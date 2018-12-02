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
    public HashMap<String,List<Info>> read(String path, String Letter) {

        HashMap<String,List<Info>> data = new HashMap<String,List<Info>>();
        try {
            BufferedReader input = new BufferedReader(new FileReader(path));
            String line = "";
            char lowerLetter = line.substring(0,1).toLowerCase().charAt(0);
            char upperLetter = line.substring(0,1).toUpperCase().charAt(0);
            while ((line = input.readLine()) != null && (line.charAt(0) == lowerLetter || line.charAt(0) == upperLetter)){
                String [] termData = line.split("|");
                List lst = null;
                data.put(termData[0],(lst = new ArrayList<>()));
                for (int i = 1; i < termData.length; i++) {
                    lst.add(new TermDocumentInfo(new Term(termData[0]), termData[i]));
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
