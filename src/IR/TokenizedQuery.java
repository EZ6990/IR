package IR;

import IO.AbstractTokenizedDocument;
import TextOperations.Token;

import java.util.List;

public class TokenizedQuery extends AbstractTokenizedDocument {

    private List<Token> Description;
    private List<Token> Narrative;

    public TokenizedQuery(String path,String ID, List<Token> text, List<Token> description, List<Token> narrative) {
        super(path,ID, text);
        Description = description;
        Narrative = narrative;
    }

    public List<Token> getDescription() {
        return Description;
    }

    public List<Token> getNarrative() {
        return Narrative;
    }
}
