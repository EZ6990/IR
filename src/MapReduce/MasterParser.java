package MapReduce;

import Main.DataProvider;
import MapReduce.Parsers.*;
import TextOperations.TokenizedDocument;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class MasterParser implements Runnable{

    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;
    private ConcurrentLinkedQueue<HashMap<String,TermDocumentInfo>> tdi_queue;
    private boolean bStop;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;

    public MasterParser(ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue,ConcurrentLinkedQueue<HashMap<String,TermDocumentInfo>> tdi_queue,Semaphore text_operation_producer,Semaphore master_parser_consumer,Semaphore master_parser_producer,Semaphore segment_file_consumer){
        this.tokenized_queue = tokenized_queue;
        this.tdi_queue = tdi_queue;
        this.bStop = false;
        this.text_operation_producer = text_operation_producer;
        this.master_parser_consumer = master_parser_consumer;
        this.master_parser_producer = master_parser_producer;
        this.segment_file_consumer = segment_file_consumer;
    }


    @Override
    public void run() {


        while (!this.bStop){
            try {
                this.master_parser_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TokenizedDocument doc = tokenized_queue.poll();
            //System.out.println("Start ID: " + doc.getID() + "  Time:" + LocalTime.now());
            this.text_operation_producer.release();
            HashMap<String, TermDocumentInfo> map = new HashMap<String, TermDocumentInfo>();
            new DatesAndRangeParser(map, doc).manipulate();
            new NumberParser(map, doc).manipulate();
            new PercentAndPriceParser(map, doc).manipulate();
            new CountryParser(map, doc).manipulate();
            new WordParser(map, doc).manipulate();

            if (map.size() > 0) {
                try {
                    this.master_parser_producer.acquire();
                    this.tdi_queue.add(map);
                    this.segment_file_consumer.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               // System.out.println("END ID: " + doc.getID() + "  Time:" + LocalTime.now());
            }
            else
                System.out.println("Document with Text Problem: " + doc.getID());
            }
    }

    public void Stop(){
        this.bStop = true;
    }
}
