package IO.Segments;

import MapReduce.Info;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SegmentCityWriter implements SegmentWriter {

    @Override
    public void write(String path,HashMap<String,List<Info>> data) {

        Object[] str = data.keySet().toArray();
        Arrays.sort(str);
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
            StringBuilder chunk = new StringBuilder();
            for (Object s : str) {
                chunk.append(s);
                for (Info tdi : data.get((String) s)) {
                    chunk.append("," + tdi.toString()).append("\n");
                }
            }
            output.write(chunk.toString());
            output.close();
        } catch (IOException e) {
        }
    }
}
