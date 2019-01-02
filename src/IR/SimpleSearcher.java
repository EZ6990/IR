package IR;

import IO.DataProvider;
import IO.Segments.SegmentTermReader;
import IO.Segments.SegmentTermWriter;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.DocumentTermInfo;
import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;
import MapReduce.Segment.TermSegmentFile;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleSearcher implements ISearcher {

    @Override
    public HashMap<AbstractTermDocumentInfo, SegmentFile> search(HashMap<String, AbstractTermDocumentInfo> parsedQuery, List<String> docs) {
        Collection<AbstractTermDocumentInfo> values = parsedQuery.values();
        HashMap<AbstractTermDocumentInfo, SegmentFile> searchedDocs = new HashMap<>();
        for (AbstractTermDocumentInfo atdi : values) {
            String termData = atdi.getTerm().getData();
            if (DataProvider.getInstance().getTermIndexer().contains(termData = termData.toLowerCase()) ||
                    DataProvider.getInstance().getTermIndexer().contains(termData = termData.toUpperCase())) {
                atdi.getTerm().setData(termData);
                String[] IndexerValue = DataProvider.getInstance().getTermIndexer().getValue(termData).split(" ");
                TermSegmentFile tsf = new TermSegmentFile(DataProvider.getInstance().getPostLocation() + "\\" + IndexerValue[0], new SegmentTermWriter(), new SegmentTermReader());
                //String[] splitedV=IndexerValue.split(" ");
                tsf.read(atdi.getTerm(), Integer.parseInt(IndexerValue[1]));
                if (docs!=null && docs.size() > 0)
                    retain(tsf.getData(), docs, termData);
                if (tsf.getData().get(termData).size() != 0)
                    searchedDocs.put(atdi, tsf);

            }
        }
        return searchedDocs;
    }

    private void retain(ConcurrentHashMap<String, List<Info>> data, List<String> docs, String termData) {
        List<Info> tmp = new ArrayList<>();
        List<Info> allDocs = data.get(termData);
        for (String docInfo : docs) {
            for (Info doc : allDocs) {
                if (((TermDocumentInfo)doc).getDocumentID().equals(docInfo)){
                    tmp.add(doc);
                    break;
                }
            }
        }
        //List<Info> allDocs = data.get(termData);
        //docs.retainAll(allDocs);
        data.put(termData, tmp);
    }
}
