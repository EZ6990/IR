package IO.Segments;

import Main.Term;
import MapReduce.AbstractTermDocumentInfo;
import MapReduce.Info;
import MapReduce.TermDocumentInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SegmentTermReader implements SegmentReader {

    @Override
    public Long read(String path, String Letter,HashMap<String,String> data,Long position) {

        try {
            //BufferedReader input = new BufferedReader(new FileReader(path));
            RandomAccessFile input = new RandomAccessFile(path,"r");
            String line = "";
            char lowerLetter = Letter.substring(0,1).toLowerCase().charAt(0);
            char upperLetter = Letter.substring(0,1).toUpperCase().charAt(0);
            input.seek(position);
            while ((line = input.readLine()) != null && (line.charAt(0) == lowerLetter || line.charAt(0) == upperLetter)){
                position += line.length() + 2;
                String [] termData = line.split(";");
                data.put(termData[0],termData[1].trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return position;
    }
}
