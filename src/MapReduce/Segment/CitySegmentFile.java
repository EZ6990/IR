package MapReduce.Segment;

import IO.Segments.SegmentReader;
import IO.Segments.SegmentWriter;
import MapReduce.Parse.CityTDI;
import MapReduce.Parse.Info;
import MapReduce.Parse.Term;
import MapReduce.Parse.TermDocumentInfo;

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
        List<String> lst=read();
        String[] termData = lst.get(position).trim().split(";");
        Term term = new Term(termData[0],null);
        String[] data = termData[1].split("\\?");
        String [] countryInfo = data[0].split("\\!");

        for (int i = 1; i < data.length; i++) {
            String [] termDocumentData = data[i].split("\\|");
            CityTDI cityInfo = new CityTDI(term,termDocumentData[0],countryInfo[0],countryInfo[1],countryInfo[2]);
            for (String location : data[i].split("\\|"))
                cityInfo.addLocation(Integer.parseInt(location));
            this.add(term.getData(),cityInfo);
        }
    }
}
