package Shell.Text.Validater;

import Shell.AutoFiller.Functions;
import Shell.Text.LetterType;

import java.util.regex.Pattern;

public class Validation {
    private static final String AND = "&";
    private static final String DIRECTORY = "[A-Za-z]+\\.txt";
    private static final String SPACE = " ";

    public static LetterType validateWord(String word){
        if(word.equals(SPACE)){
            return LetterType.NORMAL;
        } else if(Functions.isFunction(word)){
            return LetterType.FUNCTION;
        }else if(word.equals(AND)){
            return LetterType.CONNECTOR;
        }else if(Pattern.matches(DIRECTORY, word)){
            return LetterType.DIRECTORY;
        }
        return LetterType.UNKNOWN;
    }
}
