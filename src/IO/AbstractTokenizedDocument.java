package IO;

import TextOperations.Token;

import java.util.List;

public class AbstractTokenizedDocument {

    protected String path;
    protected String ID;
    protected List<Token> Text;

    public AbstractTokenizedDocument(String path, String ID, List<Token> text) {
        this.path = path;
        this.ID = ID;
        this.Text = text;
    }
    public String getID() {
        return ID;
    }

    public List<Token> getTokenizedText() {
            return Text;
        }

    public String getPath() {
        return path;
    }

}

