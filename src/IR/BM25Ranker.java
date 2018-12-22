package IR;

import IO.DataProvider;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;
import MapReduce.Segment.TermSegmentFile;

import java.util.*;

public class BM25Ranker implements IRanker {

    private final long k;
    private final float b;
    private final int avdl;
    private final int M;

    public BM25Ranker(long k, float b, int avdl,int M){
        this.k = k;
        this.b = b;
        this.avdl = avdl;
        this.M = M;
    }

    @Override
    public List<String> returnRankedDocs(HashMap<AbstractTermDocumentInfo, SegmentFile> termDocuments) {

        Collection <SegmentFile>files = termDocuments.values();


        HashMap<String,Double> documentRank = new HashMap<String, Double>();


        for (SegmentFile file: files) {
            if (file instanceof TermSegmentFile){
                TermSegmentFile termSegmentFile = (TermSegmentFile) file;
                Collection<List<Info>> termsInfo  = termSegmentFile.getData().values();
                for (List<Info> lstTermInfo : termsInfo){
                    for (Info info : lstTermInfo){
                        if (info instanceof TermDocumentInfo) {

                            //sum rank foreach term in doc
                            TermDocumentInfo termDocumentInfo = (TermDocumentInfo) info;
                            Double rank = documentRank.get(((TermDocumentInfo) info).getTerm().getData());
                            double value = calculate(termDocumentInfo);
                            rank = (rank == null ? value : rank + value);
                            documentRank.put(((TermDocumentInfo) info).getTerm().getData(),rank);

                        }
                    }
                }
            }
        }

        return null;
    }

    private Double calculate(TermDocumentInfo info){
        int wordDocumentFrequncy = Integer.parseInt(DataProvider.getInstance().getTermIndexer().getValue(info.getTerm().getData()).split(" ")[3]);
        int documentSize = Integer.parseInt(DataProvider.getInstance().getDocumentIndexer().getValue(info.getTerm().getData()).split(" ")[1]);
        return (((k + 1)*(info.getFrequency()))/(info.getFrequency() + k*(1 - b + (b*(documentSize/avdl)))))*(Math.log((M + 1)/wordDocumentFrequncy));
    }

    private int getAvdl(){

        Iterator it = DataProvider.getInstance().getDocumentIndexer().iterator();
        int total = 0;
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            total += Integer.parseInt(pair.getValue().toString().split(" ")[1]);
            it.remove();
        }

        return (total/DataProvider.getInstance().getDocumentIndexer().size());
    }
}
