package Index;

import IO.Segments.SegmentTermReader;
import IO.Segments.SegmentTermWriter;
import MapReduce.TermSegmentFile;

import java.io.*;
import java.time.LocalTime;
import java.util.HashMap;

public class Indexer {

    private HashMap<String,String> termIndex;



    public Indexer(){
        this.termIndex = new HashMap<>();
    }



    public void CreatePostFiles(String segmentLocation){

        File segmentFilesDirectory = new File(segmentLocation);
        File[] segment_sub_dirs = segmentFilesDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });

        Long [] filePositions = new Long[segment_sub_dirs.length];
        for (int i = 0; i < filePositions.length; i++) {
            filePositions[i] = new Long(0);
        }
        String [] Letters = {
                                "#","$","%","&","'","*","+",",","-",".","/","0","1","2","3","4","5","6","7","8","9","<","=",">","@",
                                "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
                                "\\","^","_","`","~"
                            };
        SegmentTermReader reader = new SegmentTermReader();
        for (int i = 0 ; i < Letters.length ; i++) {
            System.out.println(LocalTime.now() + " Start Post Letter:" + Letters[i]);
            HashMap<String, String> data = null;
            boolean bLetter = false;
            int j = 0;
            HashMap<String, String> chunkTermIndex = null;
            for (File termSegmentFile : segment_sub_dirs) {
                data = new HashMap<>();
                //TermSegmentFile termFile = new TermSegmentFile(termSegmentFile.getAbsolutePath(),null,new SegmentTermReader());
                filePositions[j] = reader.read(termSegmentFile.getAbsolutePath(),Letters[i],data,filePositions[j]);
//                    if (chunkTermIndex == null)
//                        chunkTermIndex = data;
//                    else{
////                        for (String term : data.keySet()) {
////                            bLetter = true;
////                            String lowerCaseTerm=term.toLowerCase();
////                            if (chunkTermIndex.containsKey(lowerCaseTerm))
////                                merge(lowerCaseTerm,data.get(term),chunkTermIndex);
////                            else if (chunkTermIndex.containsKey(term))
////                                merge(term,data.get(term),chunkTermIndex);
////                            else if(chunkTermIndex.containsKey(term.toUpperCase()))
////                                deleteAndMerge(term,data.get(term),chunkTermIndex);
////                            else
//                               // chunkTermIndex.put(term,data.get(term));
//                        }
//                    }
                j++;
                }
                System.out.println(LocalTime.now() + " Done Collect Letter:" + Letters[i] + " From Docs");
//                if (bLetter) {
//                    //ArrayList lst = new ArrayList(data.keySet());
//                    //Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
//                    try {
//                        System.out.println(LocalTime.now() + " Start Write Data To Disk On Letter:" + Letters[i]);
//                        int k = 0;
//                        String path = "d:\\documents\\users\\talmalu\\Documents\\Tal\\PostFile\\Collect2\\" + Letters[i];
//                        BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
//                        StringBuilder chunk = new StringBuilder();
//                        for (String s : chunkTermIndex.keySet()) {
//                            String[] forgodsake = chunkTermIndex.get(s).split("\\?");
//                            String num = forgodsake[1];
//                            chunk.append(s).append(";").append(forgodsake[0]);
//                            chunk.append("\n");
//                            chunkTermIndex.replace(s, Letters[i] + " " + k + " " + num);
//                        }
//                        output.write(chunk.toString());
//                        output.close();
//                    } catch (IOException e) {
//                    }
//                    this.termIndex.putAll(chunkTermIndex);
//                    System.out.println(LocalTime.now() + " Done Write Data To Disk On Letter:" + Letters[i]);
//                    System.out.println(LocalTime.now() + " TermIndex Size: " + this.termIndex.size());
//                }
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

}
