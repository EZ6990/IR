package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.Info;

import java.util.HashMap;
import java.util.List;

public abstract class SegmentFile{

    private String path;
    protected HashMap<String,List<Info>> data;
    private SegmentWriter writer;
    private SegmentReader reader;

    public SegmentFile(String path, SegmentWriter writer,SegmentReader reader) {
        this.path = path;
        this.data = new HashMap<>();
        this.writer = writer;
        this.reader = reader;
    }

    public abstract void add(String key,Info item);
    public boolean containsKey(String key){
        return this.data.containsKey(key);
    }
    

    public void write() {
        this.writer.write(this.path,this.data);
    }

    public List<String> read() {
        return this.reader.read(this.path);
    }

    public String getPath() {
        return this.path;
    }

}
