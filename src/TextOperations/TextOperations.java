package TextOperations;

import IO.AbstractDocument;
import IO.AbstractTokenizedDocument;
import IR.Query;
import IR.TokenizedQuery;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class TextOperations implements Runnable{

    private ConcurrentLinkedQueue<AbstractDocument> document_queue;
    private ConcurrentLinkedQueue<AbstractTokenizedDocument> tokenized_queue;
    private Tokenize tokenizer;
    private IFilter stop_words;
    private Semaphore documnet_reader_producer;
    private Semaphore text_operation_consumer;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;


    public TextOperations(ConcurrentLinkedQueue<AbstractDocument> document_queue, ConcurrentLinkedQueue<AbstractTokenizedDocument> tokenized_queue, Tokenize tokenizer, IFilter filter, Semaphore documnet_reader_producer, Semaphore text_operation_consumer, Semaphore text_operation_producer, Semaphore master_parser_consumer){
        this.document_queue = document_queue;
        this.tokenized_queue = tokenized_queue;
        this.tokenizer = tokenizer;
        this.stop_words = filter;
        this.documnet_reader_producer = documnet_reader_producer;
        this.text_operation_consumer= text_operation_consumer;
        this.text_operation_producer = text_operation_producer;
        this.master_parser_consumer = master_parser_consumer;
    }

    @Override
    public void run() {

        RemoveFromEndnStart remover = new RemoveFromEndnStart(new char[]{'.',','});
        RulesWords rules = new RulesWords();


        while (true){
            try {
                this.text_operation_consumer.acquire();
            } catch (InterruptedException e) {
                //e.printStackTrace();

            }
            AbstractDocument doc = this.document_queue.poll();
            if (doc.getPath().equals("DannyAndTalSendTheirRegardsYouFucker")){
                this.document_queue.add(doc);
                this.text_operation_consumer.release();
                break;
            }

            this.documnet_reader_producer.release();
            String path = doc.getPath();
            String ID = doc.getID();
            AbstractTokenizedDocument Adocument = null;
            List<Token> Text = remover.filter(this.stop_words.substract(rules).filter(this.tokenizer.Tokenize(doc.getText())));
            if (doc instanceof Document) {
                List<Token> Date = this.stop_words.filter(this.tokenizer.Tokenize(((Document)doc).getDate()));
                List<Token> Header = this.stop_words.filter(this.tokenizer.Tokenize(((Document)doc).getRepresentativeCountry()));
                Adocument = new TokenizedDocument(path, ID, Text, Date, Header);

            }
            else{
                List<Token> Description = this.stop_words.filter(this.tokenizer.Tokenize(((Query)doc).getDescription()));
                List<Token> Narrative = this.stop_words.filter(this.tokenizer.Tokenize(((Query)doc).getNarrative()));
                Adocument = new TokenizedQuery(path, ID, Text, Description, Narrative);
            }


            try {
                //System.out.println("Text Operation Producer : " + this.text_operation_producer.availablePermits());
                this.text_operation_producer.acquire();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            this.tokenized_queue.add(Adocument);
            this.master_parser_consumer.release();
        }
    }

}
