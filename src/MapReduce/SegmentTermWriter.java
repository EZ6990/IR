package MapReduce;


import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SegmentTermWriter {

    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, PriorityQueue<TermDocumentInfo>>> termsQueue;
    private HashMap<String, PriorityQueue<TermDocumentInfo>> postFile;

    public SegmentTermWriter(ConcurrentLinkedQueue<HashMap<String, PriorityQueue<TermDocumentInfo>>> termsQueue) {
        this.termsQueue = termsQueue;
        bStop = false;
    }

    public void write() {
        while ((postFile = termsQueue.poll()) != null && !bStop) {
            Object[] str = postFile.keySet().toArray();
            Arrays.sort(str);
            int i = 0;
            try {
                PrintWriter output = new PrintWriter(new FileWriter(Paths.get("").toAbsolutePath().toString() + "/" + i, true));
                for (Object s : str) {
                    String line = (String) s;
                    for (TermDocumentInfo tdi :
                            postFile.get((String) s)) {
                        line = line + "," + tdi.toString();
                    }

                    output.write(line + "\n");

                }
                i++;
                output.close();
            } catch (IOException e) {
            }
        }
    }
}

