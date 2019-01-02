package IO.Segments;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class SegmentTermReader implements SegmentReader {

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
