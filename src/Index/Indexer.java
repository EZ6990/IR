package Index;

import IO.Segments.SegmentTermReader;
import IO.Segments.SegmentTermWriter;
import MapReduce.TermSegmentFile;

import java.io.*;
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
        for (char i = 1 ; i <= 255 ; i++) {
            HashMap<String, String> data = null;
            int j = 0;
            for (File termSegmentFile : segment_sub_dirs) {
                data = new HashMap<>();
                TermSegmentFile termFile = new TermSegmentFile(termSegmentFile.getAbsolutePath(),new SegmentTermWriter(),new SegmentTermReader());
                filePositions[j] = termFile.read(i + "",data,filePositions[j]);
                    if (this.termIndex == null)
                        this.termIndex = data;
                    else{
                        for (String term : data.keySet()) {
                            String lowerCaseTerm=term.toLowerCase();
                            if (termIndex.containsKey(lowerCaseTerm))
                                merge(lowerCaseTerm,data.get(term));
                            else if (this.termIndex.containsKey(term))
                                merge(term,data.get(term));
                            else if(termIndex.containsKey(term.toUpperCase()))
                                deleteAndMerge(term,data.get(term));
                            else
                                termIndex.put(term,data.get(term));

                        }
                    }
                j++;
                }
                if (data.size() > 0) {
                    //ArrayList lst = new ArrayList(data.keySet());
                    //Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
                    try {
                        int k = 0;
                        String path = "d:\\documents\\users\\talmalu\\Documents\\Tal\\PostFiles\\" + i;
                        BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
                        StringBuilder chunk = new StringBuilder();
                        for (String s : this.termIndex.keySet()) {
                            if (s.toLowerCase().charAt(0) == i) {
                                String[] forgodsake = this.termIndex.get(s).split("\\?");
                                String num = forgodsake[1];
                                chunk.append(s).append(";").append(forgodsake[0]);
                                chunk.append("\n");
                                data.replace(s, i + " " + k + " " + num);
                            }
                        }
                        output.write(chunk.toString());
                        output.close();
                    } catch (IOException e) {
                    }
                }
        }
    }

    private void deleteAndMerge(String term, String s) {
        termIndex.put(term,s);
        String oldInfo=termIndex.get(term.toUpperCase());
        termIndex.remove(term.toUpperCase());
        merge(term,oldInfo);

    }

    private void merge(String term, String s) {
        String[] split=s.split("\\?");
        String[] splitMapTerm=termIndex.get(term).split("\\?");
        int sum=Integer.parseInt(split[1])+Integer.parseInt(splitMapTerm[1]);
        termIndex.put(term,split[0]+"|"+splitMapTerm[0]+"?"+sum);


    }

}
