package FileHandler.Complier.Interpreter.Token;

public class Token {
    private String name;
    private String regex;

    public Token(String name, String regex){
        this.name = name;
        this.regex = regex;
    }

    public String getSymbol() {
        return name;
    }

    public String getRegex() {
        return regex;
    }
}
