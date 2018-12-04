package MapReduce;

import IO.Segments.SegmentCityWriter;
import IO.Segments.SegmentDocumentWriter;
import IO.Segments.SegmentTermWriter;

import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SegmentFiles implements Runnable {

    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>> TDIQueue;
    private int numOfDocs;
    private ConcurrentLinkedQueue<SegmentFile> destSegmentFilesQueue;
    private int index;

    public void setThreadID(int threadID) {
        this.ThreadID = threadID;
    }

    private int ThreadID;

    private Semaphore master_parser_producer;
    private Semaphore segments_file_consumer;
    private Semaphore segment_file_term_producer;
    private Semaphore segment_writer_consumer;

    public SegmentFiles(ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>> TDIQueue, ConcurrentLinkedQueue<SegmentFile> destSegmentFilesQueue,
                        Semaphore master_parser_producer,Semaphore segments_file_consumer,Semaphore segment_file_term_producer,Semaphore segment_writer_consumer) {

        this.TDIQueue = TDIQueue;
        this.numOfDocs = 30000;
        this.bStop = false;
        this.index = 0;
        this.ThreadID = 0;

        this.destSegmentFilesQueue = destSegmentFilesQueue;
        this.master_parser_producer = master_parser_producer;
        this.segments_file_consumer = segments_file_consumer;
        this.segment_file_term_producer = segment_file_term_producer;
        this.segment_writer_consumer = segment_writer_consumer;


    }

    @Override
    public void run() {
        int mapCounter = 0;
        DocumentSegmentFile dsf = new DocumentSegmentFile("d:\\documents\\users\\talmalu\\Documents\\Tal\\DocFiles\\docs.txt",new SegmentDocumentWriter(),null);
        CitySegmentFile csf = new CitySegmentFile("d:\\documents\\users\\talmalu\\Documents\\Tal\\CityFiles\\city.txt",new SegmentCityWriter(),null);
        TermSegmentFile tsf = new TermSegmentFile("d:\\documents\\users\\talmalu\\Documents\\Tal\\SegmentFiles\\"+ this.ThreadID +"_term_" + (this.index++) + ".txt",new SegmentTermWriter(),null);
        while (!this.bStop || !this.TDIQueue.isEmpty()){
            try {
                //System.out.println("Segment File Consumer : " + this.segments_file_consumer.availablePermits());
                this.segments_file_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String, AbstractTermDocumentInfo> map = this.TDIQueue.poll();
            if (map != null) {
                //System.out.println("Segment File Producer : " + this.master_parser_producer.availablePermits());
                this.master_parser_producer.release();
                DocumentTermInfo dti = new DocumentTermInfo(((AbstractTermDocumentInfo) (map.values().toArray()[0])).getDocumentID());
                //System.out.println("Start ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
                for (String s : map.keySet()) {
                    AbstractTermDocumentInfo tdi = map.get(s);
                    if (tdi instanceof CityTDI)
                        csf.add(s, tdi);
                    tsf.add(s, tdi);

                    if (map.get(s).getFrequency() == 1)
                        dti.addRareCount();

                    if (map.get(s).getFrequency() > dti.getMostCommonFreq()) {
                        dti.setMostCommonName(s);
                        dti.setMostCommonFreq(map.get(s).getFrequency());
                    }
                }
                dsf.add(dti.getDocumentName(), dti);
                if (++mapCounter == this.numOfDocs) {
                    try {
                        //System.out.println("END ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
                        //System.out.println("Term Writer Consumer : " + this.segment_writer_consumer.availablePermits());
                        this.segment_file_term_producer.acquire();
                        this.destSegmentFilesQueue.add(tsf);
                        this.segment_writer_consumer.release();

                        this.segment_file_term_producer.acquire();
                        this.destSegmentFilesQueue.add(dsf);
                        this.segment_writer_consumer.release();

                        this.segment_file_term_producer.acquire();
                        this.destSegmentFilesQueue.add(csf);
                        this.segment_writer_consumer.release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dsf = new DocumentSegmentFile("d:\\documents\\users\\talmalu\\Documents\\Tal\\DocFiles\\docs.txt", new SegmentDocumentWriter(), null);
                    //csf = new CitySegmentFile(Paths.get("").toAbsolutePath().toString() + "\\city.txt",new SegmentCityWriter());
                    tsf = new TermSegmentFile("d:\\documents\\users\\talmalu\\Documents\\Tal\\SegmentFiles\\" + this.ThreadID + "_term_" + (this.index++) + ".txt", new SegmentTermWriter(), null);
                    mapCounter = 0;
                }
            }
        }
        try {
            //System.out.println("END ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
            //System.out.println("Term Writer Consumer : " + this.segment_writer_consumer.availablePermits());
            this.segment_file_term_producer.acquire();
            this.destSegmentFilesQueue.add(tsf);
            this.segment_writer_consumer.release();

            this.segment_file_term_producer.acquire();
            this.destSegmentFilesQueue.add(dsf);
            this.segment_writer_consumer.release();

            this.segment_file_term_producer.acquire();
            this.destSegmentFilesQueue.add(csf);
            this.segment_writer_consumer.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void Stop(){
        this.bStop = true;
    }

}
