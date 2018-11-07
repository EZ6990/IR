package TextOperations;

public class Document {

    private String path;
    private String ID;
    private String Header;
    private String Date;
    private String Text;



    public Document(String path, String ID, String Header, String date, String text) {
        this.path = path;
        this.ID = ID;
        this.Header = Header;
        this.Date = date;
        this.Text = text;
    }

    public String getPath() {
        return path;
    }

    public String getID() {
        return ID;
    }

    public String getHeader() {
        return Header;
    }

    public String getDate() {
        return Date;
    }

    public String getText() {
        return Text;
    }
}
