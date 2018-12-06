package MapReduce.Index;

import IO.DataProvider;
import IO.Segments.SegmentCityReader;
import MapReduce.Segment.CitySegmentFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class DocumentIndexer {

    private HashMap<String,String> documentIndex;

    public DocumentIndexer(){
        this.documentIndex = new HashMap<>();
    }



    public void CreatePostFiles(String segmentLocation){

        File segmentFilesDirectory = new File(segmentLocation);
        File[] segment_sub_dirs = segmentFilesDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });

        int j = 0;
        for (File termSegmentFile : segment_sub_dirs) {
            CitySegmentFile termFile = new CitySegmentFile(termSegmentFile.getAbsolutePath(),null,new SegmentCityReader());
            List<String> lstLines = termFile.read();

            for (String line : lstLines) {
                line = line.trim();
                String[] termData = line.split(";");
                String docID = termData[0];
                if (!this.documentIndex.containsKey(docID)) {
                    this.documentIndex.put(docID,termData[1]);
                }
            }
        }
//      System.out.println(LocalTime.now() + " Done Collect Letter:" + Letters[i] + " From Docs");
        try {
//          System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
            int k = 0;
            String path = DataProvider.getStopWordsLocation() + "\\doc_indexed.txt";
            BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
            StringBuilder chunk = new StringBuilder();
            for (String s : this.documentIndex.keySet()) {
                chunk.append(s).append(";").append(this.documentIndex.get(s)).append("\n");
                this.documentIndex.replace(s, "doc_indexed.txt" + " " + k);
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
