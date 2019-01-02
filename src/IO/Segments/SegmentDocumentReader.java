package IO.Segments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SegmentDocumentReader implements SegmentReader {

    @Override
    public List<String> read(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }
}
