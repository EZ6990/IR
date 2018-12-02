package MapReduce;

import java.util.PriorityQueue;
import java.util.Queue;

public class SegmentTerm {

    private Queue<TermDocumentInfo> TDIQ;
    private int totalDocumentCount;

    public SegmentTerm() {
        this.TDIQ=new PriorityQueue((o1, o2) -> (((TermDocumentInfo)o1).getTerm().getData().compareTo(((TermDocumentInfo)o2).getTerm().getData())));
        this.totalDocumentCount = 0;
    }

    public void add(TermDocumentInfo TDI){
        this.TDIQ.add(TDI);
        this.totalDocumentCount++;
    }
}
