package IR;

import IO.AbstractDocument;

public class Query extends AbstractDocument {

    private String Description;
    private String Narrative;

    public Query(String documentID, String text,String path, String description, String narrative) {
        super(documentID, text, path);
        Description = description;
        Narrative = narrative;
    }

    public String getDescription() {
        return Description;
    }

    public String getNarrative() {
        return Narrative;
    }
}
