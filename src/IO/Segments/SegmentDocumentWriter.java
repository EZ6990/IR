package IO.Segments;

import MapReduce.Info;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SegmentDocumentWriter implements SegmentWriter {


    @Override
    public void write(String path,HashMap<String, List<Info>> data) {


        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
            StringBuilder chunk = new StringBuilder();
            for (String s : data.keySet()) {
                for (Info dti : data.get(s)) {
                    chunk.append(dti.toString()).append("\n");
                }
            }
            output.write(chunk.toString());
            output.close();
        } catch (IOException e) {
        }

    }
}