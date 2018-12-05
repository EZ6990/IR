package MapReduce;

import Main.DataProvider;
import MapReduce.Parsers.*;
import TextOperations.TokenizedDocument;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class MasterParser implements Runnable{

    private ConcurrentLinkedQueue<TokenizedDocument> tokennized_queue;
    private ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>> tdi_queue;
    private boolean bStop;
    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;

    public MasterParser(ConcurrentLinkedQueue<TokenizedDocument> tokennized_queue,ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>> tdi_queue,Semaphore text_operation_producer,Semaphore master_parser_consumer,Semaphore master_parser_producer,Semaphore segment_file_consumer){
        this.tokennized_queue = tokennized_queue;
        this.tdi_queue = tdi_queue;
        this.bStop = false;
        this.text_operation_producer = text_operation_producer;
        this.master_parser_consumer = master_parser_consumer;
        this.master_parser_producer = master_parser_producer;
        this.segment_file_consumer = segment_file_consumer;
    }


    @Override
    public void run() {


        while (true) {
            try {
                this.master_parser_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TokenizedDocument doc = this.tokennized_queue.poll();

            if (doc.getPath().equals("DannyAndTalSendTheirRegardsYouFucker")){
                this.tokennized_queue.add(doc);
                this.master_parser_consumer.release();
                break;
            }
            //System.out.println("Start ID: " + doc.getID() + "  Time:" + LocalTime.now());
            this.text_operation_producer.release();
            HashMap<String, AbstractTermDocumentInfo> map = new HashMap<String, AbstractTermDocumentInfo>();
            //System.out.println("Start Date" + doc.getID());
            new DatesAndRangeParser(map, doc).manipulate();
            //System.out.println("Start Number" + doc.getID());
            new NumberParser(map, doc).manipulate();
            //System.out.println("Start Price" + doc.getID());
            new PercentAndPriceParser(map, doc).manipulate();
            //System.out.println("Start Country" + doc.getID());
            new CountryParser(map, doc).manipulate();
            //System.out.println("Start Word" + doc.getID());
            new WordParser(map, doc).manipulate();
            //System.out.println("Finish Word" + doc.getID());

            if (map.size() > 0) {
                try {
                    this.master_parser_producer.acquire();
                    this.tdi_queue.add(map);
                    this.segment_file_consumer.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println("END ID: " + doc.getID() + "  Time:" + LocalTime.now());
            } //else
                //System.out.println("Document with Text Problem: " + doc.getID());
        }
    }

    public void Stop(){
        this.bStop = true;
    }
}
