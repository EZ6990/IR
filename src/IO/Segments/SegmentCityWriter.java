package IO.Segments;

import MapReduce.Parse.Info;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SegmentCityWriter implements SegmentWriter {

    @Override
    public void write(String path, ConcurrentHashMap<String,List<Info>> data) {

        ArrayList lst = new ArrayList(data.keySet());
        Collections.sort(lst,String.CASE_INSENSITIVE_ORDER);
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true),  StandardCharsets.UTF_8));
            StringBuilder chunk = new StringBuilder();
            for (Object s : lst) {
                for (Info tdi : data.get((String) s)) {
                    chunk.append(tdi.toString()).append("\n");
                }
            }
            output.write(chunk.toString());
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
