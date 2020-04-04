package FileHandler.Complier.Interpreter;

import ProcessFormats.Opcode.Opcode;

import java.util.Arrays;
import java.util.Stack;

public class Pack {
    private String[] data;
    private static final String[][] matcher = {{"VALUE", "OPERATOR", "VALUE"}, {"VALUE", "FUNCTION"}};
    private static final Translator translator = new Translator();

    public Pack(Stack<String> codeSegments){
        this.data = pack(codeSegments);
    }

    private String[] pack(Stack<String> codeSegments){
        int size = 3;
        if(codeSegments.size() < size){
            size = codeSegments.size();
        }
        String[] segments = new String[size];
        for(int x = 0; x < size; x++){
            segments[x] = codeSegments.pop();
        }
        return segments;
    }

    public boolean isValid(){
        String[] symbols = translator.toSymbols(data);
        for(String[] specificMatcher : matcher){
            if(Arrays.equals(specificMatcher, symbols)) return true;
        }
        return false;
    }

    public Opcode toOpcode(){
        String process = translator.toOperation(data[1]);
        String[] args = new String[data.length-1];
        args[args.length-1] = data[0];
        if(args.length > 1) args[0] = data[2];
        return new Opcode(process, args);
    }
}
