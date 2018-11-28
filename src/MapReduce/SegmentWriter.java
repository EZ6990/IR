package MapReduce;


import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class SegmentWriter {


    public void write(HashMap<String, PriorityQueue<TermDocumentInfo>> postFile) {

        Object[] str = postFile.keySet().toArray();
        Arrays.sort(str);
        int i = 0;
        try {
            PrintWriter output = new PrintWriter(new FileWriter(Paths.get("").toAbsolutePath().toString() + "/" + i,true));
            for (Object s : str) {
                String line=(String)s;
                for (TermDocumentInfo tdi :
                        postFile.get((String) s)) {
                    line = line + "," + tdi.toString();
                }

                output.write(line+"\n");

            }
            i++;
            output.close();
        }catch (IOException ex) {
            System.out.println("afssgasga");
        }
    }
}

