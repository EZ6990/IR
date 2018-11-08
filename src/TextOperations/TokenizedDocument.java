package TextOperations;

import TextOperations.Document;
import TextOperations.Token;

import java.util.List;

public class TokenizedDocument {

    private String path;
    private String ID;
    private List<Token> Text;
    private List<Token> Date;
    private List<Token> Header;

    public TokenizedDocument(String path, String ID, List<Token> text, List<Token> date, List<Token> header) {
        this.path = path;
        this.ID = ID;
        this.Text = text;
        this.Date = date;
        this.Header = header;
    }

    public String getPath() {
        return path;
    }

    public String getID() {
        return ID;
    }

    public List<Token> getTokenizedText() {
        return Text;
    }

    public List<Token> getTokenizedDate() {
        return Date;
    }

    public List<Token> getTokenizedHeader() {
        return Header;
    }
}
