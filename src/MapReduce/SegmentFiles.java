package MapReduce;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SegmentFiles implements Runnable {

    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue;
    private HashMap<String, List<TermDocumentInfo>> postFile;
    private HashMap<String, DocumentTermInfo> documentPostFile;
    private int numOfDocs;
    private ConcurrentLinkedQueue<HashMap<String, List<TermDocumentInfo>>> destTermQueue;
    private ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destDocQueue;

    private Semaphore master_parser_producer;
    private Semaphore segments_file_consumer;
    private Semaphore segment_file_term_producer;
    private Semaphore segment_file_doc_producer;
    private Semaphore term_writer_consumer;
    private Semaphore doc_writer_consumer;

    public SegmentFiles(ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue, ConcurrentLinkedQueue<HashMap<String, List<TermDocumentInfo>>> destTermQueue, ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destDocQueue,
                        Semaphore master_parser_producer,Semaphore segments_file_consumer,Semaphore segment_file_term_producer,Semaphore segment_file_doc_producer,Semaphore term_writer_consumer,Semaphore doc_writer_consumer) {
        this.TDIQueue = TDIQueue;
        this.numOfDocs = 40000;
        this.postFile = new HashMap<>();
        this.bStop = false;
        this.documentPostFile = new HashMap<>();
        this.destTermQueue = destTermQueue;
        this.destDocQueue = destDocQueue;

        this.master_parser_producer = master_parser_producer;
        this.segments_file_consumer = segments_file_consumer;
        this.segment_file_term_producer = segment_file_term_producer;
        this.segment_file_doc_producer = segment_file_doc_producer;
        this.term_writer_consumer = term_writer_consumer;
        this.doc_writer_consumer = doc_writer_consumer;
    }

    @Override
    public void run() {
        int mapCounter = 0;
        while (!this.bStop){
            try {
                //System.out.println("Segment File Consumer : " + this.segments_file_consumer.availablePermits());
                this.segments_file_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String, TermDocumentInfo> map = this.TDIQueue.poll();
            //System.out.println("Segment File Producer : " + this.master_parser_producer.availablePermits());
            this.master_parser_producer.release();
            DocumentTermInfo dti = new DocumentTermInfo(((TermDocumentInfo) (map.values().toArray()[0])).getDocumentID());
            //System.out.println("Start ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
            for (String s : map.keySet()) {
                if (this.postFile.containsKey(s))
                    this.postFile.get(s).add(map.get(s));
                else {
                    List<TermDocumentInfo> queue = new ArrayList<>();
                    queue.add(map.get(s));
                    this.postFile.put(s, queue);
                }

                if (map.get(s).getFrequency() == 1)
                    dti.addRareCount();

                if (map.get(s).getFrequency() > dti.getMostCommonFreq()) {
                    dti.setMostCommonName(s);
                    dti.setMostCommonFreq(map.get(s).getFrequency());
                }
            }
            this.documentPostFile.put(dti.getDocumentName(), dti);

            if (++mapCounter == this.numOfDocs) {

                try {
                    //System.out.println("END ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
                    //System.out.println("Term Writer Consumer : " + this.term_writer_consumer.availablePermits());
                    this.segment_file_term_producer.acquire();
                    this.destTermQueue.add(this.postFile);
                    //System.out.println("Segment Term Writer Producer : " + this.segment_file_term_producer.availablePermits());
                    this.term_writer_consumer.release();
                    //System.out.println("DOC Writer Consumer : " + this.doc_writer_consumer.availablePermits());
                    this.segment_file_doc_producer.acquire();
                    this.destDocQueue.add(this.documentPostFile);
                    //System.out.println("Segment DOC Writer Producer : " + this.segment_file_doc_producer.availablePermits());
                    this.doc_writer_consumer.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.postFile = new HashMap<>();
                this.documentPostFile = new HashMap<>();
                mapCounter = 0;
            }
        }
    }

    public int getPostingSize(){
        return postFile.size();
    }
    public void Stop(){
        this.bStop = true;
    }

}
