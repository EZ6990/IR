package IO;


import TextOperations.Document;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class DocumentReader implements Runnable {

    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<Document> document_queue;
    private Semaphore document_reader_producer;
    private Semaphore text_operation_consumer;

    public DocumentReader(ConcurrentLinkedQueue<File> files_queue, ConcurrentLinkedQueue<Document> document_queue, Semaphore document_reader_producer,Semaphore text_operation_consumer){
        this.files_queue = files_queue;
        this.document_queue = document_queue;
        this.document_reader_producer = document_reader_producer;
        this.text_operation_consumer = text_operation_consumer;
    }
    @Override
    public void run() {
        File f;
        XMLReader reader;
        while ((f = this.files_queue.poll()) != null){
            try {
                reader = new XMLReader(f);
                while(reader.hasNext()) {
                    //System.out.println("Document Reader Producer : " + this.document_reader_producer.availablePermits());
                    this.document_reader_producer.acquire();
                    Document document = reader.getNextDocument();
                    if (document.getRepresentativeCountry().length() > 0){
                        DataProvider.getInstance().getFP104().put(document.getID(),document.getRepresentativeCountry());
                    }
                    if (document.getLanguage().length() > 0){
                        DataProvider.getInstance().getFP105().put(document.getID(),document.getLanguage());
                    }
                    this.document_queue.add(document);
                    //System.out.println("Text Operation Consumer : " + this.text_operation_consumer.availablePermits());
                    this.text_operation_consumer.release();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
