package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.CityTDI;
import MapReduce.Parse.Info;
import MapReduce.Parse.Term;
import MapReduce.Parse.TermDocumentInfo;

import java.util.ArrayList;
import java.util.List;

public class TermSegmentFile extends SegmentFile {

    public TermSegmentFile(String path, SegmentWriter writer,SegmentReader reader) {
        super(path, writer,reader);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof TermDocumentInfo && !(item instanceof CityTDI)) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key,(lst = new ArrayList()));
            lst.add(item);
        }
    }

    @Override
    public void read(Term key, int position) {
        List<String> lst=read();
        String[] line =lst.get(position).trim().split(";")[1].split("\\|");
        for (int i=0;i<line.length;i++)
        {
           TermDocumentInfo tdi=new TermDocumentInfo(key,line[i].split(" ")[0]);
           add(key.getData(),tdi);
        }


    }
}
