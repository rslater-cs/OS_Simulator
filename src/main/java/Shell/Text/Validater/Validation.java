package Shell.Text.Validater;

import Shell.Text.userLine.LetterType;

import java.util.regex.Pattern;

public class Validation {
    private static final String AND = "&";
    private static final String DIRECTORY = "[A-Za-z]+\\.txt";
    private static final String NUMBER = "[0-9]+";
    private static final String SPACE = " ";
    private static final String BOOLEAN = "(false)|(true)";
    private static final String[] symbols = {")", "!", "\"", "£", "$", "%", "^", "&", "*", "("};

    public static LetterType validateWord(String word){
        if(word.equals(SPACE)){
            return LetterType.NORMAL;
        } else if(Pattern.matches(BOOLEAN, word)){
            return LetterType.BOOLEAN;
        }else if(Functions.isFunction(word)){
            return LetterType.FUNCTION;
        }else if(word.equals(AND)){
            return LetterType.CONNECTOR;
        }else if(Pattern.matches(DIRECTORY, word)){
            return LetterType.DIRECTORY;
        } else if(Pattern.matches(NUMBER, word)){
            return LetterType.VALUE;
        }
        return LetterType.UNKNOWN;
    }

    public static String numberRowSymbol(String key){
        return symbols[Integer.parseInt(key)];
    }
}
