package IO.Segments;

import MapReduce.Info;

import java.util.HashMap;
import java.util.List;

public interface SegmentReader {
    public Long read(String path, String Letter,HashMap<String,String> data,Long seek);
}
