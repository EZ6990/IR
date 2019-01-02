package IR;

import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;

import java.util.HashMap;
import java.util.List;

public interface ISearcher {

    HashMap<AbstractTermDocumentInfo, SegmentFile> search(HashMap<String, AbstractTermDocumentInfo> parsedQuery, List<String> docs);
}
