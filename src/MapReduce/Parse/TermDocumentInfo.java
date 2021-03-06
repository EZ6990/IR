package MapReduce.Parse;

public class TermDocumentInfo extends AbstractTermDocumentInfo {

    public TermDocumentInfo(Term term, String documentID) {
        super(term,documentID);
    }

    @Override
    public String toString() {
        return getDocumentID()+" "+getFrequency();
    }
}
