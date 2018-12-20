package IR;

import MapReduce.Parse.TermDocumentInfo;
import MapReduce.Segment.SegmentFile;

import java.util.HashMap;
import java.util.List;

public class BM25Ranker implements IRanker{
    @Override
    public List<String> returnRankedDocs(HashMap<TermDocumentInfo, SegmentFile> termDocuments) {
        return null;
    }
}
