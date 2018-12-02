package IO.Segments;


import MapReduce.Info;

import javax.print.DocFlavor;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class SegmentTermWriter implements SegmentWriter {

    @Override
    public void write(String path,HashMap<String,List<Info>> data) {

        ArrayList lst = new ArrayList(data.keySet());
        Collections.sort(lst,String.CASE_INSENSITIVE_ORDER);
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
            StringBuilder chunk = new StringBuilder();
            for (Object s : lst) {
                chunk.append(s);
                for (Info tdi : data.get((String) s)) {
                    chunk.append("|" + tdi.toString());
                }
                chunk.append("\n");
            }
            output.write(chunk.toString());
            output.close();
        } catch (IOException e) {
        }
    }
}

