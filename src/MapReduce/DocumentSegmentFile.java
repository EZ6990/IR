package MapReduce;

import IO.Segments.SegmentWriter;

import java.util.ArrayList;
import java.util.List;

public class DocumentSegmentFile extends SegmentFile {
    public DocumentSegmentFile(String path, SegmentWriter writer) {
        super(path, writer);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof DocumentSegmentFile) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key, (lst = new ArrayList()));
            lst.add(item);
        }
    }
}
