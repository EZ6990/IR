package IR;

import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;

import java.util.HashMap;
import java.util.List;

public interface IRanker {

    public List<String> returnRankedDocs(HashMap<TermDocumentInfo, SegmentFile> termDocuments);

}
