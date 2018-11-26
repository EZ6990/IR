package MapReduce;

import TextOperations.Document;

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
    private HashMap<String, DocumentTermInfo> documentPostFile;
    private int numOfDocs;
    private SegmentWriter sWriter;
    private DocumentSegmentWriter dWriter;



    public SegmentFiles(ConcurrentLinkedQueue<HashMap<String, TermDocumentInfo>> TDIQueue) {
        this.TDIQueue = TDIQueue;
        mapCounter = 0;
        numOfDocs = 18;
        postFile = new HashMap<>();
        bStop = false;
        sWriter = new SegmentWriter();
        documentPostFile=new HashMap<>();
    }

    @Override
    public void run() {
        while ((map = TDIQueue.poll()) != null && !bStop) {
            DocumentTermInfo dti= new DocumentTermInfo(((TermDocumentInfo) (map.values().toArray()[0])).getDocumentID());

            for (String s : map.keySet()

                    ) {
                if (postFile.containsKey(s))
                    postFile.get(s).add(map.get(s));
                else {
                    postFile.put(s, new PriorityQueue<TermDocumentInfo>());
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
                sWriter.write(postFile);
                dWriter.write(documentPostFile);
                mapCounter = 0;
                postFile = new HashMap<>();
                documentPostFile=new HashMap<>();
            }

        }
    }
}
