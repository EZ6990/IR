package MapReduce.Index;

import IO.DataProvider;
import IO.Segments.SegmentCityReader;
import IO.Segments.SegmentDocumentReader;
import MapReduce.Segment.DocumentSegmentFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DocumentIndexer extends Indexer{

    public DocumentIndexer(String location){
        super(location);
    }



    public void CreatePostFiles(String postLocation){

        String prefix = DataProvider.getInstance().getPrefixPost();
        File documentSegmentFile = new File(postLocation + "\\" + prefix + "docs.txt");

        DocumentSegmentFile termFile = new DocumentSegmentFile(documentSegmentFile.getAbsolutePath(),null,new SegmentDocumentReader());
        List<String> lstLines = termFile.read();

        for (String line : lstLines) {
            line = line.trim();
            String[] termData = line.split(";");
            String docID = termData[0];
            if (!this.Index.containsKey(docID)) {
                this.Index.put(docID,termData[1]);
            }
        }

//      System.out.println(LocalTime.now() + " Done Collect Letter:" + Letters[i] + " From Docs");
        try {
//          System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
            int k = 0;
            String path = DataProvider.getInstance().getPostLocation() + "\\" + prefix + "docPost.post";
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true),  StandardCharsets.UTF_8));
            StringBuilder chunk = new StringBuilder();
            for (String s : this.Index.keySet()) {
                String data = this.Index.get(s);
                chunk.append(s).append(";").append(data).append("\n");
                this.Index.replace(s, prefix + "docPost.post" + " " + k + " " + data.split("\\|")[2]);
                k++;
            }
            output.write(chunk.toString());
            output.flush();
            output.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }

        documentSegmentFile.delete();
//      System.out.println(LocalTime.now() + " Done Write Data To Disk On Letter:" + Letters[i]);
        //   System.out.println(LocalTime.now() + " TermIndex Size: " + this.termIndex.size());
    }
}
