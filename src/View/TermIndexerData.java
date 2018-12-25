package View;

public class TermIndexerData {
    private final String term;
    private final String frequnency;


    public TermIndexerData(String term, String frequnency) {
        this.term = term;
        this.frequnency = frequnency;
    }

    public String getTerm() {
        return term;
    }

    public String getFrequnency() {
        return frequnency;
    }
}
