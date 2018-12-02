package IO.Segments;

import MapReduce.Info;

import java.util.HashMap;
import java.util.List;

public interface SegmentWriter {
    public void write (String path,HashMap<String,List<Info>> data);
}
