package IO.Segments;

import MapReduce.Parse.Info;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SegmentDocumentWriter implements SegmentWriter {


    @Override
    public void write(String path, ConcurrentHashMap<String, List<Info>> data) {


        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true),  StandardCharsets.UTF_8));
            StringBuilder chunk = new StringBuilder();
            for (String s : data.keySet()) {
                for (Info dti : data.get(s)) {
                    chunk.append(dti.toString()).append("\n");
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