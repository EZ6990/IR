package TextOperations;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class TextOperations implements Runnable{

    private ConcurrentLinkedQueue<Document> document_queue;
    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;
    private Tokenize tokenizer;
    private IFilter stop_words;
    private boolean bStop;
    private Semaphore documnet_reader_producer;
    private Semaphore text_operation_consumer;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;


    public TextOperations(ConcurrentLinkedQueue<Document> document_queue, ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue, Tokenize tokenizer, IFilter filter,Semaphore documnet_reader_producer,Semaphore text_operation_consumer,Semaphore text_operation_producer,Semaphore master_parser_consumer){
        this.document_queue = document_queue;
        this.tokenized_queue = tokenized_queue;
        this.tokenizer = tokenizer;
        this.stop_words = filter;
        this.bStop = false;
        this.documnet_reader_producer = documnet_reader_producer;
        this.text_operation_consumer= text_operation_consumer;
        this.text_operation_producer = text_operation_producer;
        this.master_parser_consumer = master_parser_consumer;
    }

    @Override
    public void run() {

        while (!this.bStop){
            try {
                this.text_operation_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Document doc = document_queue.poll();
            this.documnet_reader_producer.release();
            String path = doc.getPath();
            String ID = doc.getID();
            List<Token> Text = this.stop_words.filter(this.tokenizer.Tokenize(doc.getText()));
            List<Token> Date = this.stop_words.filter(this.tokenizer.Tokenize(doc.getDate()));
            List<Token> Header = this.stop_words.filter(this.tokenizer.Tokenize(doc.getHeader()));

            try {
                this.text_operation_producer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.tokenized_queue.add(new TokenizedDocument(path, ID, Text, Date, Header));
            this.master_parser_consumer.release();
        }
    }

    public void Stop(){
        this.bStop = true;
    }
}
