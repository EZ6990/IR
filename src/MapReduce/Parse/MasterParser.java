package MapReduce.Parse;

import IO.AbstractTokenizedDocument;
import MapReduce.Parse.Parsers.*;
import TextOperations.IFilter;
import TextOperations.Stemmer;
import TextOperations.TokenizedDocument;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class MasterParser implements Runnable{

    private ConcurrentLinkedQueue<AbstractTokenizedDocument> tokennized_queue;
    private ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>> tdi_queue;

    private Semaphore text_operation_producer;
    private Semaphore master_parser_consumer;
    private Semaphore master_parser_producer;
    private Semaphore segment_file_consumer;
    private Stemmer stemmer;
    private IFilter ignore;

    public MasterParser(ConcurrentLinkedQueue<AbstractTokenizedDocument> tokennized_queue,ConcurrentLinkedQueue<HashMap<String,AbstractTermDocumentInfo>> tdi_queue,Semaphore text_operation_producer,Semaphore master_parser_consumer,Semaphore master_parser_producer,Semaphore segment_file_consumer,Stemmer stemmer,IFilter ignore){
        this.tokennized_queue = tokennized_queue;
        this.tdi_queue = tdi_queue;

        this.text_operation_producer = text_operation_producer;
        this.master_parser_consumer = master_parser_consumer;
        this.master_parser_producer = master_parser_producer;
        this.segment_file_consumer = segment_file_consumer;
        this.stemmer = stemmer;
        this.ignore = ignore;
    }


    @Override
    public void run() {

        while (true) {
            try {
                this.master_parser_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AbstractTokenizedDocument doc = this.tokennized_queue.poll();

            if (doc.getPath().equals("DannyAndTalSendTheirRegardsYouFucker")){
                this.tokennized_queue.add(doc);
                this.master_parser_consumer.release();
                break;
            }
//            System.out.println("Start ID: " + doc.getID() + "  Time:" + LocalTime.now());
            this.text_operation_producer.release();
            HashMap<String, AbstractTermDocumentInfo> map = new HashMap<String, AbstractTermDocumentInfo>();
          // System.out.println("Start Date" + doc.getID());
            new DatesAndRangeParser(map, doc,this.stemmer).manipulate();
//            System.out.println("Start Number" + doc.getID());
            new NumberParser(map, doc,this.stemmer).manipulate();
//            System.out.println("Start Price" + doc.getID());
            new PercentAndPriceParser(map, doc,this.stemmer).manipulate();
//            System.out.println("Start Country" + doc.getID());
            new CountryParser(map, doc,this.stemmer).manipulate();
//           System.out.println("Start Word" + doc.getID());
            new WordParser(map, doc,this.stemmer,this.ignore).manipulate();
//            System.out.println("Finish Word" + doc.getID());

            if (map.size() > 0) {
                try {
                //    System.out.println("Master Parser Producer : " + this.master_parser_producer.availablePermits());
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

}
