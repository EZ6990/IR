package IR;

import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Segment.SegmentFile;

import java.util.HashMap;
import java.util.List;

public interface IRanker {

    public List<String> returnRankedDocs(HashMap<AbstractTermDocumentInfo, SegmentFile> termDocuments);

}
