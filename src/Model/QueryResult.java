package Model;

import java.io.*;
import java.util.List;

public class QueryResult {
    private String queryId;
    private int iter;
    private List<String> docno;
    private int rank;
    private float sim;
    private String run_id;


    public QueryResult(String queryId, int iter,int rank, float sim, String run_id) {
        this.queryId = queryId;
        this.iter = iter;
        this.rank=rank;
        this.sim = sim;
        this.run_id = run_id;
    }


    public void writeToPath(File f) throws IOException {

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        StringBuilder chunk = new StringBuilder();
        for (int i = 0; i < docno.size(); i++) {
            chunk.append(queryId+" "+iter+" "+docno.get(i)+" "+rank+" "+sim+" "+run_id);
        }
        chunk.append(queryId+" "+iter+" "+docno+" "+rank+" "+sim+" "+run_id);
        output.write(chunk.toString());
    }
}
