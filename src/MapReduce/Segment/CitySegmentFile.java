package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.CityTDI;
import MapReduce.Parse.Info;
import MapReduce.Parse.Term;

import java.util.ArrayList;
import java.util.List;

public class CitySegmentFile extends SegmentFile {

    public CitySegmentFile(String path, SegmentWriter writer, SegmentReader reader) {
        super(path, writer,reader);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof CityTDI) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key, (lst = new ArrayList()));
            lst.add(item);
        }
    }

    @Override
    public void read(Term key, int position) {

    }
}
