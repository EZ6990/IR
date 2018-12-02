package IO.Segments;

import MapReduce.Info;

import java.util.HashMap;
import java.util.List;

public interface SegmentReader {
    public HashMap<String,List<Info>> read(String path, String Letter);
}
