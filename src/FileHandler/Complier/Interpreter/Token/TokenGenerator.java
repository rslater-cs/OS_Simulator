package FileHandler.Complier.Interpreter.Token;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenGenerator {
    ArrayList<Token> tokens = new ArrayList<>();

    public void add(String name, String regex){
        if(name != null && regex != null){
            tokens.add(new Token(name, regex));
        }
    }

    public String toSymbol(String segment){
        for(Token token : tokens){
            if(Pattern.matches(token.getRegex(), segment)) return token.getSymbol();
        }
        return "NONE";
    }
}
