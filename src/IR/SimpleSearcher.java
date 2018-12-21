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
    public HashMap<AbstractTermDocumentInfo, SegmentFile> search(HashMap<String, AbstractTermDocumentInfo> parsedQuery, List<Info> docs) {
        Collection<AbstractTermDocumentInfo> values = parsedQuery.values();
        HashMap<AbstractTermDocumentInfo, SegmentFile> searchedDocs = new HashMap<>();
        for (AbstractTermDocumentInfo atdi :
                values) {
            String termData = atdi.getTerm().getData();
            if (DataProvider.getInstance().getTermIndexer().contains(termData = termData.toLowerCase()) ||
                    DataProvider.getInstance().getTermIndexer().contains(termData = termData.toUpperCase())) {
                atdi.getTerm().setTerm(termData);
                TermSegmentFile tsf = new TermSegmentFile(DataProvider.getInstance().getPostLocation(), new SegmentTermWriter(), new SegmentTermReader());
                String IndexerValue = DataProvider.getInstance().getTermIndexer().getValue(termData);
                //String[] splitedV=IndexerValue.split(" ");
                tsf.read(IndexerValue.charAt(0) + "", Integer.parseInt(IndexerValue.substring(1, IndexerValue.indexOf('.'))));
                retain(tsf.getData(), docs, termData);
                if (tsf.getData().get(termData).size() != 0)
                    searchedDocs.put(atdi, tsf);

            }

        }

        return searchedDocs;
    }

    private void retain(ConcurrentHashMap<String, List<Info>> data, List<Info> docs, String termData) {
        List<Info> allDocs = data.get(termData);
        docs.retainAll(allDocs);
        data.put(termData, docs);
    }


}
