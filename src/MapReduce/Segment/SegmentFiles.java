package MapReduce.Segment;

import IO.DataProvider;
import IO.Segments.SegmentCityWriter;
import IO.Segments.SegmentDocumentWriter;
import IO.Segments.SegmentTermWriter;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.CityTDI;
import MapReduce.Parse.DocumentTermInfo;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SegmentFiles implements Runnable {

    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>> TDIQueue;
    private int numOfDocs;
    private ConcurrentLinkedQueue<SegmentFile> destSegmentFilesQueue;
    private int i;

    public void setThreadID(int threadID) {
        this.ThreadID = threadID;
    }

    private int ThreadID;

    private Semaphore master_parser_producer;
    private Semaphore segments_file_consumer;
    private Semaphore segment_file_term_producer;
    private Semaphore segment_writer_consumer;

    public SegmentFiles(ConcurrentLinkedQueue<HashMap<String, AbstractTermDocumentInfo>> TDIQueue, ConcurrentLinkedQueue<SegmentFile> destSegmentFilesQueue,
                        Semaphore master_parser_producer, Semaphore segments_file_consumer, Semaphore segment_file_term_producer, Semaphore segment_writer_consumer) {

        this.TDIQueue = TDIQueue;
        this.numOfDocs = 32200;
        this.bStop = false;
        this.ThreadID = 0;
        this.i = 0;
        this.destSegmentFilesQueue = destSegmentFilesQueue;
        this.master_parser_producer = master_parser_producer;
        this.segments_file_consumer = segments_file_consumer;
        this.segment_file_term_producer = segment_file_term_producer;
        this.segment_writer_consumer = segment_writer_consumer;


    }

    @Override
    public void run() {
        int mapCounter = 0;
        String postLocation = DataProvider.getInstance().getPostLocation();
        String prefix = DataProvider.getInstance().getPrefixPost();
        String[] Letters = {
                "#", "$", "%", "&", "'", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "<", "=", ">", "@",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "\\", "^", "_", "`", "~"
        };
        HashMap<String, TermSegmentFile> tsfa = new HashMap<String, TermSegmentFile>();
        int index = 0;
        for (String s : Letters) {
            tsfa.put(s, new TermSegmentFile(postLocation + "\\" + prefix + "" + this.ThreadID + i + "_" + index++ + ".txt", new SegmentTermWriter(), null));
        }

        DocumentSegmentFile dsf = new DocumentSegmentFile(postLocation + "\\" + prefix + "docs.txt", new SegmentDocumentWriter(), null);
        CitySegmentFile csf = new CitySegmentFile(postLocation + "\\" + prefix + "city.txt", new SegmentCityWriter(), null);


        while (true) {
            try {
                //  System.out.println("Segment File Consumer : " + this.segments_file_consumer.availablePermits());
                this.segments_file_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String, AbstractTermDocumentInfo> map = this.TDIQueue.poll();
           // System.out.println("Segment File Producer : " + this.master_parser_producer.availablePermits());
            if (map.containsKey("DannyAndTalSendTheirRegardsYouFucker")) {
                this.TDIQueue.add(map);
                this.segments_file_consumer.release();
                break;
            }
            this.master_parser_producer.release();
            DocumentTermInfo dti = new DocumentTermInfo(((AbstractTermDocumentInfo) (map.values().toArray()[0])).getDocumentID());
            dti.setNumOfDifferentWords(map.size());
            setEntities(dti, map);
           // System.out.println("Start ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
            long start = System.currentTimeMillis();
            for (String s : map.keySet()) {
                AbstractTermDocumentInfo tdi = map.get(s);
                String termWord = tdi.getTerm().getData();

                if (tdi instanceof CityTDI)
                    csf.add(termWord, tdi);
                if (tsfa.containsKey(termWord.substring(0, 1).toLowerCase()))
                    tsfa.get(termWord.substring(0, 1).toLowerCase()).add(termWord, tdi);

                if (map.get(s).getFrequency() > dti.getMostCommonFreq()) {
                    dti.setMostCommonName(s);
                    dti.setMostCommonFreq(map.get(s).getFrequency());
                }
                dti.addToNumOfTerms(tdi.getFrequency());

            }
            //System.out.println("END ID: " + dti.getDocumentName() + "  Took : " + (System.currentTimeMillis() - start));
            dsf.add(dti.getDocumentName(), dti);
            if (++mapCounter == this.numOfDocs) {
                try {
                    //System.out.println("END ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
                    //System.out.println("Term Writer Consumer : " + this.segment_writer_consumer.availablePermits());
                    for (int i = 0; i < Letters.length; i++) {
                        TermSegmentFile tsf = tsfa.get(Letters[i]);
                        if (tsf.data.size() > 0) {
                            this.segment_file_term_producer.acquire();
                            this.destSegmentFilesQueue.add(tsf);
                            this.segment_writer_consumer.release();
                        }
                    }

                    this.segment_file_term_producer.acquire();
                    this.destSegmentFilesQueue.add(dsf);
                    this.segment_writer_consumer.release();

                    this.segment_file_term_producer.acquire();
                    this.destSegmentFilesQueue.add(csf);
                    this.segment_writer_consumer.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                index = 0;
                for (String s : Letters) {
                    tsfa.put(s, new TermSegmentFile(postLocation + "\\" + prefix + this.ThreadID+i++ + "_" + index++ + ".txt", new SegmentTermWriter(), null));
                }
                csf = new CitySegmentFile(postLocation + "\\" + prefix + "city.txt", new SegmentCityWriter(), null);
                dsf = new DocumentSegmentFile(postLocation + "\\" + prefix + "docs.txt", new SegmentDocumentWriter(), null);
                mapCounter = 0;
            }
        }
        try {
            //System.out.println("END ID: " + dti.getDocumentName() + "  Time:" + LocalTime.now());
            //System.out.println("Term Writer Consumer : " + this.segment_writer_consumer.availablePermits());
            //System.out.println(tsf.getPath() + " " + tsf.data.size());
            for (int i = 0; i < Letters.length; i++) {
                TermSegmentFile tsf = tsfa.get(Letters[i]);
                if (tsf.data.size() > 0) {
                    this.segment_file_term_producer.acquire();
                    this.destSegmentFilesQueue.add(tsf);
                    this.segment_writer_consumer.release();
                }
            }

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

    private void setEntities(DocumentTermInfo dti, HashMap<String, AbstractTermDocumentInfo> map) {
        Queue enties = new PriorityQueue((o1, o2) -> ((AbstractTermDocumentInfo) o1).getFrequency() - ((AbstractTermDocumentInfo) o2).getFrequency());
        for (AbstractTermDocumentInfo atdi : map.values()) {
            enties.add(atdi);
        }
        if (enties.size() > 0) {
            for (int i = 0; i < 5; i++) {
                AbstractTermDocumentInfo tmp = (AbstractTermDocumentInfo) enties.poll();
                if (tmp != null) {
                    String data = tmp.getTerm().getData();
                    if (data.charAt(0) >= 'A' && data.charAt(0) <= 'Z')
                        dti.addEntities(data);
                }
            }
        }
    }

    public void Stop() {
        this.bStop = true;
    }

}
