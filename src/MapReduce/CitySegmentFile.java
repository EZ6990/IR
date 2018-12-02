package MapReduce;

import IO.Segments.SegmentWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitySegmentFile extends SegmentFile {

    public CitySegmentFile(String path, SegmentWriter writer) {
        super(path, writer);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof CityTDI) {
            List lst = data.get(key);
            if (lst == null) {
                data.put(key, (lst = new ArrayList()));
                lst.add(item);
            }
        }
    }
}
