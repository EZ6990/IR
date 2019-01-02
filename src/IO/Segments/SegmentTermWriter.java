package IO.Segments;


import MapReduce.Parse.Info;
import MapReduce.Parse.TermDocumentInfo;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SegmentTermWriter implements SegmentWriter {

    @Override
    public void write(String path, ConcurrentHashMap<String,List<Info>> data) {

        ArrayList lst = new ArrayList(data.keySet());
        Collections.sort(lst,String.CASE_INSENSITIVE_ORDER);
        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true),  StandardCharsets.UTF_8));
            StringBuilder chunk = new StringBuilder();
            for (Object s : lst) {
                chunk.append(s).append(";");
                int sum = 0;
                for (Info tdi : data.get((String) s)) {
                    sum +=((TermDocumentInfo)tdi).getFrequency();
                    chunk.append(tdi.toString()).append("|");
                }
                chunk.deleteCharAt(chunk.length() - 1).append("?").append(sum);
                chunk.append("\n");
            }
            output.write(chunk.toString());
            output.flush();
            output.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }
}

