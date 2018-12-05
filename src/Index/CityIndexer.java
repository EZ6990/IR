package Index;

import IO.Segments.SegmentCityReader;
import MapReduce.CitySegmentFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class CityIndexer {

    private HashMap<String,String> cityIndex;

    public CityIndexer(){
        this.cityIndex = new HashMap<>();
    }



    public void CreatePostFiles(String segmentLocation){

        File segmentFilesDirectory = new File(segmentLocation);
        File[] segment_sub_dirs = segmentFilesDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });
        //System.out.println(LocalTime.now() + " Start Post Letter:" + Letters[i]);

        int j = 0;
        for (File termSegmentFile : segment_sub_dirs) {
            CitySegmentFile termFile = new CitySegmentFile(termSegmentFile.getAbsolutePath(),null,new SegmentCityReader());
            List<String> lstLines = termFile.read();

            for (String line : lstLines) {
                line = line.trim();
                String[] termData = line.split(";");
                String cityName = termData[0];
                termData = termData[1].split("\\?");
                if (!this.cityIndex.containsKey(cityName)) {
                    String[] countyData = termData[0].split(" ");
                    this.cityIndex.put(cityName,countyData[0] + " " + countyData[1] + " " + countyData[2]);
                }
                StringBuilder tmp = new StringBuilder();
                tmp.append(this.cityIndex.get(cityName)).append(termData[1]);
                this.cityIndex.replace(cityName,tmp.toString());
            }

        }
//      System.out.println(LocalTime.now() + " Done Collect Letter:" + Letters[i] + " From Docs");
        try {
//          System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
            int k = 0;
            String path = "D:\\documents\\users\\talmalu\\Documents\\Tal\\CiryFile\\city_indexed.txt";
            BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
            StringBuilder chunk = new StringBuilder();
            for (String s : this.cityIndex.keySet()) {
                chunk.append(s).append(";").append(this.cityIndex.get(s)).append("\n");
                this.cityIndex.replace(s, "city_indexed.txt" + " " + k);
                k++;
            }
            output.write(chunk.toString());
            output.flush();
            output.close();
        } catch (IOException e) {
        }
//      System.out.println(LocalTime.now() + " Done Write Data To Disk On Letter:" + Letters[i]);
    //   System.out.println(LocalTime.now() + " TermIndex Size: " + this.termIndex.size());
    }
}
