package Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class QueryResult {
    private String queryId;
    private int iter;
    private List<String> docno;
    private int rank;
    private int sim;
    private String run_id;


    public QueryResult(String queryId, int iter,int rank, int sim, String run_id) {
        this.queryId = queryId;
        this.iter = iter;
        this.rank=rank;
        this.sim = sim;
        this.run_id = run_id;
    }

    public QueryResult(String queryId, int iter,int rank, int sim, String run_id, List<String> docno) {
        this.queryId = queryId;
        this.iter = iter;
        this.rank=rank;
        this.sim = sim;
        this.run_id = run_id;
        this.docno=docno;
    }


    public void writeToPath(File f) throws IOException {

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f,true),  StandardCharsets.UTF_8));
        StringBuilder chunk = new StringBuilder();
        List <String>ans=this.docno.subList(0,this.docno.size() >= 50 ? 50 : this.docno.size());
        //ans.sort(String::compareTo);
        for (int i = 0; i < ans.size(); i++) {
            chunk.append(queryId+" "+iter+" "+ans.get(i)+" "+rank+" "+sim+" "+run_id);
            chunk.append("\r\n");

        }

        output.write(chunk.toString());
        output.flush();
        output.close();
    }

    public String getQueryId() {
        return this.queryId;
    }

    public List<String > getDocs() {
        return this.docno;
    }
}
