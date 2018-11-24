package MapReduce;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SegmentFiles implements Runnable {

    private boolean bStop;
    private HashMap<String, TermDocumentInfo> map;
    private int mapCounter;
    private ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue;
    private HashMap<String, Queue<TermDocumentInfo>> postFile;
    private int numOfDocs;
    private SegmentWriter writer;


    public SegmentFiles(ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue) {
        this.TDIQueue = TDIQueue;
        mapCounter = 0;
        numOfDocs = 18;
        postFile = new HashMap<>();
        bStop=false;
        writer=new SegmentWriter();
    }

    @Override
    public void run() {
        while ((map = TDIQueue.poll()) != null && !bStop) {
            for (String s : map.keySet()

                    ) {
                if (postFile.containsKey(s))
                    postFile.get(s).add(map.get(s));
                else {
                    postFile.put(s, new PriorityQueue<TermDocumentInfo>());
                }

                if (++mapCounter == numOfDocs) {
                    writer.write(postFile);
                    mapCounter=0;
                    postFile=new HashMap<>();
                }


            }

        }
    }
}
