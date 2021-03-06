package View;

public class TermIndexerData {
    private final String term;
    private final int frequency;


    public TermIndexerData(String term, String frequnency) {
        this.term = term;
        this.frequency = Integer.parseInt(frequnency);
    }
    public TermIndexerData(String term, int frequnency) {
        this.term = term;
        this.frequency = frequnency;
    }

    public String getTerm() {
        return term;
    }

    public int getFrequency() {
        return frequency;
    }
}
