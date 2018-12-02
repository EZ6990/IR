package MapReduce;

import IO.Segments.SegmentWriter;

import java.util.ArrayList;
import java.util.List;

public class TermSegmentFile extends SegmentFile {

    public TermSegmentFile(String path, SegmentWriter writer) {
        super(path, writer);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof TermSegmentFile && !(item instanceof CityTDI)) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key,(lst = new ArrayList()));
            lst.add(item);
        }
    }
}