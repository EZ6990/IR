package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.DocumentTermInfo;
import MapReduce.Parse.Info;

import java.util.ArrayList;
import java.util.List;

public class DocumentSegmentFile extends SegmentFile {
    public DocumentSegmentFile(String path, SegmentWriter writer, SegmentReader reader) {
        super(path, writer,reader);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof DocumentTermInfo) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key, (lst = new ArrayList()));
            lst.add(item);
        }
    }

}
