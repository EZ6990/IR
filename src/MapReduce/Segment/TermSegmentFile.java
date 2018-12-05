package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.CityTDI;
import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;

import java.util.ArrayList;
import java.util.List;

public class TermSegmentFile extends SegmentFile {

    public TermSegmentFile(String path, SegmentWriter writer,SegmentReader reader) {
        super(path, writer,reader);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof TermDocumentInfo && !(item instanceof CityTDI)) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key,(lst = new ArrayList()));
            lst.add(item);
        }
    }
}
