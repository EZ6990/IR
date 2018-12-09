package TextOperations;

public class Document {

    private String path;
    private String ID;
    private String Country;
    private String Date;
    private String Text;
    private String Language;



    public Document(String path, String ID, String Country, String date, String text,String language) {
        this.path = path;
        this.ID = ID;
        this.Country = Country;
        this.Date = date;
        this.Text = text;
        this.Language = language;
    }

    public String getPath() {
        return path;
    }

    public String getID() {
        return ID;
    }

    public String getRepresentativeCountry() {
        return Country;
    }

    public String getDate() {
        return Date;
    }

    public String getText() {
        return Text;
    }

    public String getLanguage() {
        return this.Language;
    }
}
