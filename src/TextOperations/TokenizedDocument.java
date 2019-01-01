package TextOperations;

import IO.AbstractTokenizedDocument;
import TextOperations.Document;
import TextOperations.Token;

import java.util.List;

public class TokenizedDocument extends AbstractTokenizedDocument {

    private String path;
    private List<Token> Date;
    private List<Token> Header;

    public TokenizedDocument(String path, String ID, List<Token> text, List<Token> date, List<Token> header) {
        super(path,ID,text);
        this.path = path;
        this.Date = date;
        this.Header = header;
    }

    public String getPath() {
        return path;
    }

    public List<Token> getTokenizedDate() {
        return Date;
    }

    public List<Token> getTokenizedHeader() {
        return Header;
    }
}
