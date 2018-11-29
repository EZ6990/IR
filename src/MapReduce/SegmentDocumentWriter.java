package MapReduce;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SegmentDocumentWriter implements Runnable{
    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> docQueue;
    private Semaphore segment_file_doc_producer;
    private Semaphore doc_writer_consumer;

    public SegmentDocumentWriter(ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destQueue,Semaphore segment_file_doc_producer,Semaphore doc_writer_consumer) {
        this.docQueue = destQueue;
        this.bStop = false;
        this.segment_file_doc_producer = segment_file_doc_producer;
        this.doc_writer_consumer = doc_writer_consumer;
    }

    public void write(HashMap<String, DocumentTermInfo> postFile) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(Paths.get("").toAbsolutePath().toString() + "/docPost", true));
            StringBuilder chunk = new StringBuilder();
            for (DocumentTermInfo dti : postFile.values()) {
                chunk.append(dti.toString()).append("\n");
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
                this.doc_writer_consumer.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String, DocumentTermInfo> postFile = docQueue.poll();
            this.segment_file_doc_producer.release();
            write(postFile);
        }
    }

    public void Stop(){
        this.bStop = true;
    }
}