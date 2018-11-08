package IO;


import TextOperations.Document;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DocumentReader implements Runnable {

    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<Document> document_queue;

    public DocumentReader(ConcurrentLinkedQueue<File> files_queue,ConcurrentLinkedQueue<Document> document_queue){
        this.files_queue = files_queue;
        this.document_queue = document_queue;
    }
    @Override
    public void run() {
        File f;
        XMLReader reader;
        while ((f = files_queue.poll()) != null){
            try {
                reader = new XMLReader(f);
                Document document = reader.getNextDocument();
                document_queue.add(document);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
