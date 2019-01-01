package IO;

public class AbstractDocument {

    protected String path;
    protected String ID;
    protected String Text;

    public AbstractDocument(String documentID, String text,String path) {
        this.ID = documentID;
        this.Text = text;
        this.path = path;
    }

    public String getID() {
        return ID;
    }

    public void setID(String documentID) {
        ID = documentID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getPath() {
        return path;
    }
}
