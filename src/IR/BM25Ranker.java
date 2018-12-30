package IR;

import IO.DataProvider;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;
import MapReduce.Segment.TermSegmentFile;

import java.util.*;

public class BM25Ranker implements IRanker {

    private final double k;
    private final float b;
    private final int avdl;
    private final int M;

    public BM25Ranker(double k, float b, int avdl,int M){
        this.k = k;
        this.b = b;
        this.avdl = avdl;
        this.M = M;
    }

    @Override
    public List<String> returnRankedDocs(HashMap<AbstractTermDocumentInfo, SegmentFile> termDocuments) {

        Collection <SegmentFile>files = termDocuments.values();


        HashMap<String,Double> documentRank = new HashMap<String, Double>();

        for (Map.Entry<AbstractTermDocumentInfo,SegmentFile> pair : termDocuments.entrySet()){
            AbstractTermDocumentInfo queryTerm = pair.getKey();
            SegmentFile file = pair.getValue();
            if (file instanceof TermSegmentFile){
                TermSegmentFile termSegmentFile = (TermSegmentFile) file;
                Collection<List<Info>> termsInfo  = termSegmentFile.getData().values();
                for (List<Info> lstTermInfo : termsInfo){
                    for (Info info : lstTermInfo){
                        if (info instanceof AbstractTermDocumentInfo) {
                            //sum rank foreach term in doc
                            AbstractTermDocumentInfo ATermDocumentInfo = (AbstractTermDocumentInfo) info;
                            Double rank = documentRank.get(ATermDocumentInfo.getTerm().getData());
                            double value = calculate(queryTerm,ATermDocumentInfo);
                            rank = (rank == null ? value : rank + value);
                            documentRank.put(ATermDocumentInfo.getDocumentID(),rank);
                        }
                    }
                }
            }
        }

        List<Map.Entry<String,Double>> lstDucomentRank = new ArrayList<>(documentRank.entrySet());

        Collections.sort(lstDucomentRank, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (Double.compare(o1.getValue() , o2.getValue()));
            }
        });

        ArrayList<String> ans = new ArrayList();
        lstDucomentRank.forEach(stringDoubleEntry -> ans.add(stringDoubleEntry.getKey()));
        return ans;
    }

    private Double calculate(AbstractTermDocumentInfo queryTerm, AbstractTermDocumentInfo info){
        int wordDocumentFrequncy = Integer.parseInt(DataProvider.getInstance().getTermIndexer().getValue(info.getTerm().getData()).split(" ")[3]);
        int documentSize = Integer.parseInt(DataProvider.getInstance().getDocumentIndexer().getValue(info.getDocumentID()).split(" ")[2]);
        return (queryTerm.getFrequency())*(((k + 1)*(info.getFrequency()))/(info.getFrequency() + k*(1 - b + (b*(documentSize/avdl)))))*(Math.log((M + 1)/wordDocumentFrequncy));
    }
}
