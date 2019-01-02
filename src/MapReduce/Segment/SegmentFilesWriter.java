package MapReduce.Segment;

import MapReduce.Segment.SegmentFile;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class SegmentFilesWriter implements Runnable {


    private boolean bStop;
    private ConcurrentLinkedQueue<SegmentFile> filesQueue;
    private Semaphore segment_file_term_producer;
    private Semaphore segment_writer_consumer;

    public SegmentFilesWriter(ConcurrentLinkedQueue<SegmentFile> filesQueue, Semaphore segment_file_term_producer, Semaphore segment_writer_consumer) {
        this.bStop = false;
        this.filesQueue = filesQueue;
        this.segment_file_term_producer = segment_file_term_producer;
        this.segment_writer_consumer = segment_writer_consumer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.segment_writer_consumer.acquire();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            SegmentFile file = filesQueue.poll();
            if (file.getPath().equals("DannyAndTalSendTheirRegardsYouFucker")){
                this.filesQueue.add(file);
                this.segment_writer_consumer.release();
                break;
            }
            this.segment_file_term_producer.release();
            file.write();
        }
    }


    public void Stop() {
        this.bStop = true;
    }
}