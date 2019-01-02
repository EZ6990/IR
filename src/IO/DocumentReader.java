package IO;


import TextOperations.Document;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class DocumentReader implements Runnable {

    private ConcurrentLinkedQueue<File> files_queue;
    private ConcurrentLinkedQueue<AbstractDocument> document_queue;
    private Semaphore document_reader_producer;
    private Semaphore text_operation_consumer;
    private XMLReaderFactory xmlReaderFactory;
    private XMLReader.Type readerType;

    public DocumentReader(ConcurrentLinkedQueue<File> files_queue, ConcurrentLinkedQueue<AbstractDocument> document_queue, Semaphore document_reader_producer,Semaphore text_operation_consumer,XMLReader.Type readerType){
        this.files_queue = files_queue;
        this.document_queue = document_queue;
        this.document_reader_producer = document_reader_producer;
        this.text_operation_consumer = text_operation_consumer;
        this.xmlReaderFactory = new XMLReaderFactory();
        this.readerType = readerType;

    }
    @Override
    public void run() {
        File f;
        XMLReader reader;
        while ((f = this.files_queue.poll()) != null){
            try {
                reader = xmlReaderFactory.getXMLReader(this.readerType,f);
                while(reader.hasNext()) {
                    //System.out.println("Document Reader Producer : " + this.document_reader_producer.availablePermits());
                    this.document_reader_producer.acquire();
                    AbstractDocument document = reader.getNextDocument();
                    if (this.readerType.equals(XMLReader.Type.DOCUMENT)) {
                        if (((Document)document).getRepresentativeCountry().length() > 0) {
                            DataProvider.getInstance().getFP104().put(document.getID(), ((Document)document).getRepresentativeCountry());
                        }
                        if (((Document)document).getLanguage().length() > 0) {
                            DataProvider.getInstance().getFP105().put(document.getID(), ((((Document)document).getLanguage())));
                        }
                    }
                    this.document_queue.add(document);
                    //System.out.println("Text Operation Consumer : " + this.text_operation_consumer.availablePermits());
                    this.text_operation_consumer.release();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }
}
