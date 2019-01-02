package IR;

import IO.DataProvider;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;
import MapReduce.Segment.TermSegmentFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BM25Ranker implements IRanker {

    private final double k;
    private final double b;
    private final double avdl;
    private final int M;

    public BM25Ranker(double k, double b, double avdl,int M){
        this.k = k;
        this.b = b;
        this.avdl = avdl;
        this.M = M;
    }

    @Override
    public List<String> returnRankedDocs(HashMap<AbstractTermDocumentInfo, SegmentFile> termDocuments,HashMap<String,Double> weight) {

        Collection <SegmentFile>files = termDocuments.values();


        HashMap<String, BigDecimal> documentRank = new HashMap<String, BigDecimal>();

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
                            BigDecimal rank = documentRank.get(ATermDocumentInfo.getDocumentID());
                            BigDecimal value = calculate(queryTerm,ATermDocumentInfo,weight);
                            rank = (rank == null ? value : rank.add(value));
                            documentRank.put(ATermDocumentInfo.getDocumentID(),rank);
                        }
                    }
                }
            }
        }

        List<Map.Entry<String,BigDecimal>> lstDucomentRank = new ArrayList<>(documentRank.entrySet());

        Collections.sort(lstDucomentRank, new Comparator<Map.Entry<String, BigDecimal>>() {
            @Override
            public int compare(Map.Entry<String, BigDecimal> o1, Map.Entry<String, BigDecimal> o2) {
                return (o2.getValue().compareTo(o1.getValue()));
            }
        });

        ArrayList<String> ans = new ArrayList();
        lstDucomentRank.forEach(stringDoubleEntry -> ans.add(stringDoubleEntry.getKey()));
        return ans;
    }

    private BigDecimal calculate(AbstractTermDocumentInfo queryTerm, AbstractTermDocumentInfo info,HashMap<String,Double> weight){

        double wordWeight = 1;
        int wordDocumentFrequency = Integer.parseInt(DataProvider.getInstance().getTermIndexer().getValue(info.getTerm().getData()).split(" ")[3]);
        int documentSize = Integer.parseInt(DataProvider.getInstance().getDocumentIndexer().getValue(info.getDocumentID()).split(" ")[2]);

        if (weight != null)
            wordWeight = weight.get(info.getTerm().getData().toUpperCase());
        BigDecimal x = new BigDecimal(queryTerm.getFrequency()).multiply(new BigDecimal(k + 1)).multiply(new BigDecimal(info.getFrequency())).multiply(new BigDecimal(wordWeight));
        BigDecimal y = new BigDecimal(info.getFrequency()).add(new BigDecimal(k).multiply(new BigDecimal(1 - b)).add(new BigDecimal(b).multiply(new BigDecimal(documentSize).divide(new BigDecimal(avdl),32,RoundingMode.HALF_UP))));
        BigDecimal log = new BigDecimal(Math.log10((new BigDecimal(M + 1).divide((new BigDecimal(wordDocumentFrequency)),32,RoundingMode.HALF_UP)).doubleValue()));

//      return new BigDecimal((queryTerm.getFrequency())*(((k + 1)*(info.getFrequency()))/(info.getFrequency() + k*(1 - b + (b*(documentSize/avdl))))))*(Math.log10((M + 1)/wordDocumentFrequency)));
        return x.divide(y,32,RoundingMode.HALF_UP).multiply(log);
    }
}
