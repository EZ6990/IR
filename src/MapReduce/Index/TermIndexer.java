package MapReduce.Index;

import IO.DataProvider;
import IO.Segments.SegmentTermReader;
import MapReduce.Segment.TermSegmentFile;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TermIndexer extends Indexer{


    public TermIndexer(String location){
        super(location);
    }




    public void CreatePostFiles(String segmentLocation){

        String postLocation = DataProvider.getPostLocation();
        File segmentFilesDirectory = new File(segmentLocation);
        File[] segment_sub_dirs = segmentFilesDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });

        String [] Letters = {
                "#","$","%","&","'","*","+",",","-",".","/","0","1","2","3","4","5","6","7","8","9","<","=",">","@",
                "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
                "\\","^","_","`","~"
        };
        for (int i = 0 ; i < Letters.length ; i++) {
            HashMap<String, String> chunkTermIndex = new HashMap<>();
//            System.out.println(LocalTime.now() + " Start Post Letter:" + Letters[i]);
            boolean bLetter = false;
            int j = 0;
            for (File termSegmentFile : segment_sub_dirs) {
                if (!termSegmentFile.getName().endsWith("_" + i + ".txt"))
                    continue;
                TermSegmentFile termFile = new TermSegmentFile(termSegmentFile.getAbsolutePath(),null,new SegmentTermReader());
                List<String> lstLines = termFile.read();


                for (String line : lstLines) {
                    String [] termData = line.split(";");
                    String term=termData[0];
                    String info=termData[1].trim();
                    bLetter = true;
                    String lowerCaseTerm=term.toLowerCase();
                    if (chunkTermIndex.containsKey(lowerCaseTerm))
                        merge(lowerCaseTerm,info,chunkTermIndex);
                    else if (chunkTermIndex.containsKey(term))
                        merge(term,info,chunkTermIndex);
                    else if(chunkTermIndex.containsKey(term.toUpperCase()))
                        deleteAndMerge(term,info,chunkTermIndex);
                    else
                        chunkTermIndex.put(term,info);

                }
                j++;
            }
//                System.out.println(LocalTime.now() + " Done Collect Letter:" + Letters[i] + " From Docs");
            if (bLetter) {
                //ArrayList lst = new ArrayList(data.keySet());
                //Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
                try {
//                        System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
                    int k = 0;
                    String path = postLocation + "\\" + i;
                    BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
                    StringBuilder chunk = new StringBuilder();
                    for (String s : chunkTermIndex.keySet()) {
                        String[] forgodsake = chunkTermIndex.get(s).split("\\?");
                        String num = forgodsake[1];
                        String[] splitToCount=forgodsake[0].split("|");
                        int numOfDocs=splitToCount.length;
                        chunk.append(s).append(";").append(forgodsake[0]);
                        chunk.append("\n");
                        chunkTermIndex.replace(s, i + " " + k + " " + num + " " + numOfDocs);
                        k++;
                    }
                    output.write(chunk.toString());
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.Index.putAll(chunkTermIndex);
//                    System.out.println(LocalTime.now() + " Done Write Data To Disk On Letter:" + Letters[i]);
//                    System.out.println(LocalTime.now() + " TermIndex Size: " + this.termIndex.size());
            }
        }
    }

    private void deleteAndMerge(String term, String s,HashMap<String,String> chunkTermIndex) {
        chunkTermIndex.put(term,s);
        String oldInfo=chunkTermIndex.get(term.toUpperCase());
        chunkTermIndex.remove(term.toUpperCase());
        merge(term,oldInfo,chunkTermIndex);

    }

    private void merge(String term, String s,HashMap<String,String> chunkTermIndex) {
        String[] split=s.split("\\?");
        String[] splitMapTerm=chunkTermIndex.get(term).split("\\?");
        int sum=Integer.parseInt(split[1])+Integer.parseInt(splitMapTerm[1]);
        chunkTermIndex.put(term,(new StringBuilder(split[0]).append("|").append(splitMapTerm[0]).append("?").append(sum)).toString());

    }

    public HashMap<String,String> getTermNumberOfOccurrenceMap(){
        HashMap <String,String> map = new HashMap<String,String>();
        String [] terms =(String [])this.Index.keySet().toArray();
        Arrays.sort(terms,String.CASE_INSENSITIVE_ORDER);

        for (String term: terms) {
            String [] termData = ((String)this.Index.get(term)).split(" ");
            map.put(term,termData[2]);
        }
        return map;
    }
}
