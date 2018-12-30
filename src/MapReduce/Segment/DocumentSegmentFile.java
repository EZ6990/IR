package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.CityTDI;
import MapReduce.Parse.DocumentTermInfo;
import MapReduce.Parse.Info;
import MapReduce.Parse.Term;
import TextOperations.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentSegmentFile extends SegmentFile {
    public DocumentSegmentFile(String path, SegmentWriter writer, SegmentReader reader) {
        super(path, writer,reader);
    }

    @Override
    public void add(String key,Info item) {
        if (item instanceof DocumentTermInfo) {
            List lst = data.get(key);
            if (lst == null)
                data.put(key, (lst = new ArrayList()));
            lst.add(item);
        }
    }

    @Override
    public void read(Term key, int position) {
        List<String> lst=read();
        String[] termData = lst.get(position).trim().split(";");
        String DocumentID = termData[0];
        DocumentTermInfo info = new DocumentTermInfo(DocumentID);
        String[] data = termData[1].split("\\|");
        info.setMostCommonFreq(Integer.parseInt(data[0]));
        info.setNumOfDifferentWords(Integer.parseInt(data[1]));
        info.addToNumOfTerms(Integer.parseInt(data[2]));
        if(data.length == 4){
            String [] entities = data[3].split("\\!");
            for (String entity : entities)
                info.addEntities(entity);
        }
        this.add(DocumentID,info);
    }

}
