package TextOperations;

public class Document {

    private String path;
    private String ID;
    private String Country;
    private String Date;
    private String Text;



    public Document(String path, String ID, String Country, String date, String text) {
        this.path = path;
        this.ID = ID;
        this.Country = Country;
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
        return Country;
    }

    public String getDate() {
        return Date;
    }

    public String getText() {
        return Text;
    }
}
