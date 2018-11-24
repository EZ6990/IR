package MapReduce;

import MapReduce.Parsers.DatesAndRangeParser;
import MapReduce.Parsers.NumberParser;
import MapReduce.Parsers.PercentAndPriceParser;
import MapReduce.Parsers.WordParser;
import TextOperations.TokenizedDocument;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MasterParser implements Runnable{

    private ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue;
    private ConcurrentLinkedQueue<HashMap<String,TermDocumentInfo>> tdi_queue;
    private boolean bStop;

    public MasterParser(ConcurrentLinkedQueue<TokenizedDocument> tokenized_queue,ConcurrentLinkedQueue<HashMap<String,TermDocumentInfo>> tdi_queue){
        this.tokenized_queue = tokenized_queue;
        this.tdi_queue = tdi_queue;
        this.bStop = false;
    }


    @Override
    public void run() {

        TokenizedDocument doc;
        while ((doc = tokenized_queue.poll()) != null && !bStop){
            HashMap<String, TermDocumentInfo> map = new HashMap<String, TermDocumentInfo>();
            new DatesAndRangeParser(map,doc).manipulate();
            new NumberParser(map,doc).manipulate();
            new DatesAndRangeParser(map,doc).manipulate();
            new PercentAndPriceParser(map,doc).manipulate();
            new WordParser(map,doc).manipulate();

            tdi_queue.add(map);

        }
    }
}
