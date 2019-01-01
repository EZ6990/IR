package TextOperations;

import IO.AbstractDocument;

public class Document extends AbstractDocument {

    private String Country;
    private String Date;
    private String Language;



    public Document(String path, String ID, String Country, String date, String text,String language) {
        super(ID,text,path);
        this.Country = Country;
        this.Date = date;
        this.Language = language;
    }

    public String getPath() {
        return path;
    }

    public String getRepresentativeCountry() {
        return Country;
    }

    public String getDate() {
        return Date;
    }

    public String getLanguage() {
        return this.Language;
    }
}
