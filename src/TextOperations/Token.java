package TextOperations;

public class Token {

    private String Word;

    public Token(String text){
        this.Word = text;
    }

    public boolean isNumber() {
        try {
            Double.parseDouble(this.Word);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    public String getWord() {
        return Word;
    }

    @Override
    public String toString() {
        return this.Word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return this.Word != null ? this.Word.equals(token.Word) : token.Word == null;
    }

    @Override
    public int hashCode() {
        return this.Word != null ? this.Word.hashCode() : 0;
    }
}
