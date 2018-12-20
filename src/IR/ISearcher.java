package IR;

import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;

import java.util.HashMap;

public interface ISearcher {

    public HashMap<TermDocumentInfo, SegmentFile> search(HashMap<String, AbstractTermDocumentInfo> parsedQuery);
}
