package IO.Segments;

import MapReduce.Parse.Info;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface SegmentWriter {
    public void write (String path, ConcurrentHashMap<String,List<Info>> data);
}
