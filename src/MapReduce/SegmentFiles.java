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
        this.mapCounter = 0;
        this.numOfDocs = 18;
        this.postFile = new HashMap<>();
        this.bStop = false;
        this.documentPostFile = new HashMap<>();
        this.destTermQueue = destTermQueue;
        this.destDocQueue = destDocQueue;
    }

    @Override
    public void run() {
        while ((this.map = this.TDIQueue.poll()) != null || !this.bStop){

            if (this.map != null) {
                DocumentTermInfo dti = new DocumentTermInfo(((TermDocumentInfo) (this.map.values().toArray()[0])).getDocumentID());
                for (String s : this.map.keySet()

                        ) {
                    if (this.postFile.containsKey(s))
                        this.postFile.get(s).add(this.map.get(s));
                    else {
                        this.postFile.put(s, new PriorityQueue<TermDocumentInfo>(Comparator.comparing(o -> o.getDocumentID())));
                    }

                    if (this.map.get(s).getFrequency() == 1)
                        dti.addRareCount();

                    if (this.map.get(s).getFrequency() > dti.getMostCommonFreq()) {
                        dti.setMostCommonName(s);
                        dti.setMostCommonFreq(this.map.get(s).getFrequency());
                    }
                }

                this.documentPostFile.put(dti.getDocumentName(), dti);

                if (++this.mapCounter == this.numOfDocs) {
                    this.destTermQueue.add(this.postFile);
                    this.destDocQueue.add(this.documentPostFile);
                    this.mapCounter = 0;
                    this.postFile = new HashMap<>();
                    this.documentPostFile = new HashMap<>();
                }
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
