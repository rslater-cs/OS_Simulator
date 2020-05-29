package filehandler.compiler.interpreter;

import filehandler.compiler.Variables;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class Pack {
    private Operand[] tokens;
    private Variables variables;
    private final static String[] REGEX = {"(print)|(input)", "[a-zA-Z][a-zA-z0-9]*", "[0-9]+"};
    private final static AddressMode[] ADDRESS_MODE = {AddressMode.NONE, AddressMode.DIRECT, AddressMode.IMMEDIATE};
    private final static Map<String, Opcode> operations = new HashMap<>();

    public Pack(Stack<String> codeSegments, Variables variables){
        this.variables = variables;
        this.tokens = toArguments(pack(codeSegments));
        operations.put("*", Opcode.MUL);
        operations.put("-", Opcode.SUB);
        operations.put("+", Opcode.ADD);
        operations.put("/", Opcode.DIV);
        operations.put("=", Opcode.STR);
        operations.put("print", Opcode.OUT);
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

    private Operand[] toArguments(String[] data){
        Operand[] operands = new Operand[data.length];
        for(int x = 0; x < operands.length; x++){
            operands[x] = toArgument(data[x]);
        }
        return operands;
    }

    private Operand toArgument(String name){
        AddressMode addressMode = getAddressMode(name);
        if(addressMode == AddressMode.DIRECT){
            name = variables.replace(name);
        }
        return new Operand(name, addressMode);
    }

    private AddressMode getAddressMode(String name){
        if(name.equals("ret")) return AddressMode.REGISTER;
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

    public Instruction toOpcode(){
        Opcode process = toOperation(tokens[1].getValue());
        Operand[] args = new Operand[tokens.length-1];
        args[args.length-1] = new Operand(tokens[0].getValue(), tokens[0].getAddressMode());
        if(args.length > 1) args[0] = new Operand(tokens[2].getValue(), tokens[2].getAddressMode());
        return new Instruction(process, args);
    }

    private Opcode toOperation(String process){
        if(operations.containsKey(process)) return operations.get(process);
        return Opcode.ERR;
    }
}
