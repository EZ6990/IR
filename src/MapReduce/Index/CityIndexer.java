package MapReduce.Index;

import IO.DataProvider;
import IO.Segments.SegmentCityReader;
import MapReduce.Segment.CitySegmentFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class CityIndexer extends Indexer{

    public CityIndexer(String location){
        super(location);
    }



    public void CreatePostFiles(String postLocation){

        File citySegmentFile = new File(postLocation + "\\city.txt");
        CitySegmentFile termFile = new CitySegmentFile(citySegmentFile.getAbsolutePath(),null,new SegmentCityReader());
        List<String> lstLines = termFile.read();

        for (String line : lstLines) {
            line = line.trim();
            String[] termData = line.split(";");
            String cityName = termData[0];
            termData = termData[1].split("\\?");
            if (!this.Index.containsKey(cityName)) {
                String[] countyData = termData[0].split("!");
                this.Index.put(cityName,countyData[0] + "!" + countyData[1] + "!" + countyData[2]);
            }
            StringBuilder tmp = new StringBuilder();
            tmp.append(this.Index.get(cityName)).append("?").append(termData[1]);
            this.Index.replace(cityName,tmp.toString());
        }

//      System.out.println(LocalTime.now() + " Done Collect Letter:" + Letters[i] + " From Docs");
        try {
//          System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
            int k = 0;
            String path = postLocation + "\\city_indexed.txt";
            BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
            StringBuilder chunk = new StringBuilder();
            for (String s : this.Index.keySet()) {
                chunk.append(s).append(";").append(this.Index.get(s)).append("\n");
                this.Index.replace(s, "city_indexed.txt" + " " + k);
                k++;
            }
            output.write(chunk.toString());
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//      System.out.println(LocalTime.now() + " Done Write Data To Disk On Letter:" + Letters[i]);
    //   System.out.println(LocalTime.now() + " TermIndex Size: " + this.termIndex.size());
    }
}
