package MapReduce;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SegmentDocumentWriter {
    private boolean bStop;
    private ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> docQueue;
    private HashMap<String, DocumentTermInfo> docsToFile;

    public SegmentDocumentWriter(ConcurrentLinkedQueue<HashMap<String, DocumentTermInfo>> destQueue) {
        this.docQueue = destQueue;
        bStop = false;
    }

    public void write() {
        while ((docsToFile = docQueue.poll()) != null && !bStop) {
            try {
                PrintWriter output = new PrintWriter(new FileWriter(Paths.get("").toAbsolutePath().toString() + "/docPost", true));
                for (DocumentTermInfo dti : docsToFile.values()
                        ) {
                    String line = dti.toString();
                    output.write(line + "\n");
                }
                output.close();
            } catch (IOException e) {
            }
        }
    }
}