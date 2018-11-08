package TextOperations.Parsers;

import TextOperations.Token;

import java.util.List;

public class NumberParser extends AbstractParser {


    @Override
    public void manipulate() {
        int i = 0;
        int size = getTxtSize();
        String s = "";
        Token token;
        Token nextToken;


        while (i < size - 1) {
            token =get(i);
            nextToken =get(i + 1);
            if (token.isNumber()) {

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
                putInMap(s);
            }

            i++;
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
