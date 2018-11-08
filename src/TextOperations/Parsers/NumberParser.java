package TextOperations.Parsers;

import Main.Term;
import TextOperations.Token;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class NumberParser extends AbstractParser {


    @Override
    public void manipulate() {
        List<Token> txt = getTxt();
        int i = 0;
        int size = txt.size();
        String s = "";
        Token token;
        Token nextToken;


        while (i < size - 1) {
            token = txt.get(i);
            nextToken = txt.get(i + 1);
            if (token.isNumber()) {
                i++;
                s = convertNumber(token.getString());
                if (nextToken.isNumber())
                {
                    //check shever
                    //i++;
                }
                else if (nextToken.getString().equals("Thousand")||nextToken.getString().equals("Million")||nextToken.getString().equals("Billion")) {
                        s = s + nextToken.getString().charAt(0);
                        i++;
                    }

                else if (nextToken.getString().equals("Trillion")) {
                        s = Double.parseDouble(s) * 1000 + "B";
                        i++;
                }
            }
        }


    }

    private String convertNumber(String string) {
        //TODO check 3/4
        double num = Double.parseDouble(string);
        if (num >= 1000000000)
            return ("" + num / 1000000000 + "B");
        else if (num >= 1000000)
            return ("" + num / 1000000 + "M");
        else if (num >= 1000)
            return ("" + num / 1000 + "K");

        return ""+num;
    }



}
