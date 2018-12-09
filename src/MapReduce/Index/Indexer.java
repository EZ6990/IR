package MapReduce.Index;

import IO.DataProvider;
import IO.Segments.SegmentTermReader;
import MapReduce.Segment.TermSegmentFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public abstract class Indexer {

    private String location;
    protected HashMap<String,String> Index;

    public Indexer(String location){
        this.location = location;
        this.Index = new HashMap<>();
    }


    public abstract void CreatePostFiles(String segmentLocation);

    public void read(){
        try {
             List <String> lst = Files.readAllLines(Paths.get(this.location));
            for (String line : lst){
                line.trim();
                String [] tempData = line.split(";");
                this.Index.put(tempData[0],tempData[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(){

        try {
//          System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.location, true),  StandardCharsets.UTF_8));
            StringBuilder chunk = new StringBuilder();
            for (String s : this.Index.keySet()) {
                chunk.append(s).append(";").append(this.Index.get(s)).append("\n");
            }
            output.write(chunk.toString());
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int size(){
        return this.Index.size();
    }
}
