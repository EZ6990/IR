package MapReduce;

import IO.Segments.SegmentWriter;

import java.util.HashMap;
import java.util.List;

public abstract class SegmentFile{

    private String path;
    protected HashMap<String,List<Info>> data;
    private SegmentWriter writer;

    public SegmentFile(String path, SegmentWriter writer) {
        this.path = path;
        this.data = new HashMap<>();
        this.writer = writer;
    }

    public abstract void add(String key,Info item);
    public boolean containsKey(String key){
        return this.data.containsKey(key);
    }
    

    public void write() {
        this.writer.write(this.path,this.data);
    }

}
