package MapReduce;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SegmentFiles implements Runnable {

    private boolean bStop;
    private HashMap<String, TermDocumentInfo> map;
    private int mapCounter;
    private ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue;
    private HashMap<String, PriorityQueue<TermDocumentInfo>> postFile;
    private HashMap<String, DocumentTermInfo> documentPostFile;
    private int numOfDocs;
    private ConcurrentLinkedQueue<HashMap<String, PriorityQueue<TermDocumentInfo>>> destTermQueue;
    private ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destDocQueue;

    public SegmentFiles(ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue, ConcurrentLinkedQueue<HashMap<String, PriorityQueue<TermDocumentInfo>>> destTermQueue, ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destDocQueue) {
        this.TDIQueue = TDIQueue;
        mapCounter = 0;
        numOfDocs = 18;
        postFile = new HashMap<>();
        bStop = false;
        documentPostFile = new HashMap<>();
        this.destTermQueue = destTermQueue;
        this.destDocQueue = destDocQueue;
    }

    @Override
    public void run() {
        while ((map = TDIQueue.poll()) != null && !bStop) {
            DocumentTermInfo dti = new DocumentTermInfo(((TermDocumentInfo) (map.values().toArray()[0])).getDocumentID());
            for (String s : map.keySet()

                    ) {
                if (postFile.containsKey(s))
                    postFile.get(s).add(map.get(s));
                else {
                    postFile.put(s, new PriorityQueue<TermDocumentInfo>(Comparator.comparing(o -> o.getDocumentID())));
                }

                if (map.get(s).getFrequency() == 1)
                    dti.addRareCount();

                if (map.get(s).getFrequency() > dti.getMostCommonFreq()) {
                    dti.setMostCommonName(s);
                    dti.setMostCommonFreq(map.get(s).getFrequency());
                }


            }

            documentPostFile.put(dti.getDocumentName(), dti);

            if (++mapCounter == numOfDocs) {
                destTermQueue.add(postFile);
                destDocQueue.add(documentPostFile);
                mapCounter = 0;
                postFile = new HashMap<>();
                documentPostFile = new HashMap<>();
            }

        }
    }
}
