package TextOperations;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TextOperations implements Runnable{

    private ConcurrentLinkedQueue<Document> document_queue;
    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;
    private Tokenize tokenizer;
    private IFilter stop_words;
    private boolean bStop;

    public TextOperations(ConcurrentLinkedQueue<Document> document_queue, ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue, Tokenize tokenizer, IFilter filter){
        this.document_queue = document_queue;
        this.tokenized_queue = tokenized_queue;
        this.tokenizer = tokenizer;
        this.stop_words = filter;
        this.bStop = false;
    }

    @Override
    public void run() {

        Document doc;
        while ((doc = document_queue.poll()) != null || !this.bStop){
            if (doc != null) {
                String path = doc.getPath();
                String ID = doc.getID();
                List<Token> Text = this.stop_words.filter(this.tokenizer.Tokenize(doc.getText()));
                List<Token> Date = this.stop_words.filter(this.tokenizer.Tokenize(doc.getDate()));
                List<Token> Header = this.stop_words.filter(this.tokenizer.Tokenize(doc.getHeader()));

                tokenized_queue.add(new TokenizedDocument(path, ID, Text, Date, Header));
            }

        }
    }

    public void Stop(){
        this.bStop = true;
    }
}
