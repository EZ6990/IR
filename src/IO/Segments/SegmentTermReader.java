package IO.Segments;

import java.io.*;
import java.util.HashMap;

public class SegmentTermReader implements SegmentReader {

    @Override
    public Long read(String path, String Letter,HashMap<String,String> data,Long position) {

        String line = "";
        try {
            //BufferedReader input = new BufferedReader(new FileReader(path));
            RandomAccessFile input = new RandomAccessFile(path,"r");
            char lowerLetter = Letter.substring(0,1).toLowerCase().charAt(0);
            char upperLetter = Letter.substring(0,1).toUpperCase().charAt(0);
            input.seek(position);
            while ((line = input.readLine()) != null && line.trim().length() > 0 && (line.charAt(0) == lowerLetter || line.charAt(0) == upperLetter)){
                position += line.length() + 2;
                String [] termData = line.split(";");
                data.put(termData[0],termData[1].trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            System.out.println(line);
        }
        return position;
    }
}
