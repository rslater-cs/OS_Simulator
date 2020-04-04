package FileHandler.Complier.Interpreter;

import FileHandler.Complier.Interpreter.Token.TokenGenerator;

import java.util.HashMap;
import java.util.Map;

public class Translator {
    private static final TokenGenerator tokenGenerator = new TokenGenerator();
    private static final Map<String, String> operations = new HashMap<>();

    public Translator(){
        tokenGenerator.add("FUNCTION", "(print)|(input)");
        tokenGenerator.add("OPERATOR", "[\\+|\\-|\\*|\\/|=]");
        tokenGenerator.add("VALUE", "([a-zA-Z][a-zA-Z0-9]*)|([0-9]+)");
        operations.put("*", "mul");
        operations.put("/", "div");
        operations.put("+", "add");
        operations.put("-", "sub");
        operations.put("=", "str");
        operations.put("print", "out");
        operations.put("input", "inp");
    }

    public String[] toSymbols(String[] codeSegments){
        String[] symbols = new String[codeSegments.length];
        for(int x = 0; x<codeSegments.length; x++){
            symbols[x] = tokenGenerator.toSymbol(codeSegments[x]);
        }
        return symbols;
    }

    public String toOperation(String operator){
        return operations.get(operator);
    }
}
