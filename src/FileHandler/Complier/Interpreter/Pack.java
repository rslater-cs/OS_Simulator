package FileHandler.Complier.Interpreter;

import FileHandler.Complier.Interpreter.Token.Token;
import FileHandler.Complier.Variables;
import ProcessFormats.Opcode.AddressMode;
import ProcessFormats.Opcode.Argument;
import ProcessFormats.Opcode.Opcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class Pack {
    private Token[] tokens;
    private Variables variables;
    private final static String[] REGEX = {"(print)|(input)", "[a-zA-Z][a-zA-z0-9]*", "[0-9]+"};
    private final static AddressMode[] ADDRESS_MODE = {AddressMode.NONE, AddressMode.DIRECT, AddressMode.IMMEDIATE};
    private final static Map<String, String> operations = new HashMap<>();

    public Pack(Stack<String> codeSegments, Variables variables){
        this.variables = variables;
        this.tokens = toTokens(pack(codeSegments));
        operations.put("*", "mul");
        operations.put("-", "sub");
        operations.put("+", "add");
        operations.put("/", "div");
        operations.put("=", "str");
        operations.put("print", "out");
        operations.put("input", "inp");
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

    private Token[] toTokens(String[] data){
        Token[] tokens = new Token[data.length];
        for(int x = 0; x < tokens.length; x++){
            tokens[x] = toToken(data[x]);
        }
        return tokens;
    }

    private Token toToken(String name){
        AddressMode addressMode = getAddressMode(name);
        //System.out.println(addressMode);
        //System.out.println(name);
        if(addressMode == AddressMode.DIRECT){
            name = variables.replace(name);
        }
        return new Token(addressMode, name);
    }

    private AddressMode getAddressMode(String name){
        for(int x = 0; x < REGEX.length; x++){
            if(Pattern.matches(REGEX[x], name)){
                return ADDRESS_MODE[x];
            }
        }
        return ADDRESS_MODE[0];
    }

    public boolean validate(){
        return (isFunction() || isOperation());
    }

    private boolean isFunction(){
        if(tokens.length < 2) return false;
        return (tokens[0].getAddressMode() != AddressMode.NONE && tokens[1].getAddressMode() == AddressMode.NONE);
    }

    private boolean isOperation(){
        if(tokens.length < 3) return false;
        return (tokens[0].getAddressMode() != AddressMode.NONE && tokens[1].getAddressMode() == AddressMode.NONE && tokens[2].getAddressMode() != AddressMode.NONE);
    }

    public Opcode toOpcode(){
        String process = toOperation(tokens[1].getValue());
        Argument[] args = new Argument[tokens.length-1];
        args[args.length-1] = new Argument(tokens[0].getValue(), tokens[0].getAddressMode());
        if(args.length > 1) args[0] = new Argument(tokens[2].getValue(), tokens[2].getAddressMode());
        return new Opcode(process, args);
    }

    private String toOperation(String process){
        if(operations.containsKey(process)) return operations.get(process);
        return process;
    }
}
