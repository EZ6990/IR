package IO.Segments;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SegmentTermReader implements SegmentReader {

    @Override
    public Long read(String path, String Letter,HashMap<String,String> data,Long position) {

        String line = "";
        try {
            File [] files;
            //For (File file :)
            List<String> list = Files.read(Paths.get(path));
            Collections.sort(list);
            //BufferedReader input = new BufferedReader(new FileReader(path));
            RandomAccessFile input = new RandomAccessFile(path,"r");
            char lowerLetter = Letter.substring(0,1).toLowerCase().charAt(0);
            char upperLetter = Letter.substring(0,1).toUpperCase().charAt(0);
            //input.seek(position);
            //while ((line = input.readLine()) != null && line.trim().length() > 0 && (line.charAt(0) == lowerLetter || line.charAt(0) == upperLetter)){
            while ((line = input.readLine()) != null){
                position += line.length() + 1;
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
