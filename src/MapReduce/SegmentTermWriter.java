package MapReduce;


import java.io.*;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SegmentTermWriter implements Runnable{

    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, List<TermDocumentInfo>>> termsQueue;
    private int index;
    private Semaphore segment_file_term_producer;
    private Semaphore term_writer_consumer;

    public SegmentTermWriter(ConcurrentLinkedQueue<HashMap<String, List<TermDocumentInfo>>> termsQueue,Semaphore segment_file_term_producer,Semaphore term_writer_consumer) {
        this.termsQueue = termsQueue;
        this.bStop = false;
        this.index = 0;
        this.segment_file_term_producer = segment_file_term_producer;
        this.term_writer_consumer = term_writer_consumer;
    }

    public void write(HashMap<String, List<TermDocumentInfo>> postFile) {



        Object[] str = postFile.keySet().toArray();
        Arrays.sort(str);
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(Paths.get("").toAbsolutePath().toString() + "/" + index++, true));
            StringBuilder chunk = new StringBuilder();
            for (Object s : str) {
                chunk.append(s);
                //String line = (String) s;
                for (TermDocumentInfo tdi : postFile.get((String) s)) {
                    chunk.append("," + tdi.toString());
                }
                chunk.append("\n");
            }
            output.write(chunk.toString());
            output.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        while (!this.bStop) {
            try {
                this.term_writer_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String, List<TermDocumentInfo>> postFile = termsQueue.poll();
            this.segment_file_term_producer.release();
            write(postFile);
        }
    }
    public void Stop(){
        this.bStop = true;
    }
}

