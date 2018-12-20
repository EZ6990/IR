package IR;

import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;

import java.util.HashMap;

public class SimpleSearcher implements ISearcher{

    @Override
    public HashMap<TermDocumentInfo, SegmentFile> search(HashMap<String, AbstractTermDocumentInfo> parsedQuery) {
        return null;
    }



}
