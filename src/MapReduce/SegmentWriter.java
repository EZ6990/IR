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
            FileOutputStream fos = new FileOutputStream(Paths.get("").toAbsolutePath().toString() + "/" + i);
            for (Object s : str) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = null;

                out = new ObjectOutputStream(bos);
                out.writeObject(postFile.get((String) s));
                out.flush();
                byte[] bytes = bos.toByteArray();


                fos.write(bytes);
                i++;
            }
        }catch (IOException ex) {
            System.out.println("afssgasga");
        }
    }
}

