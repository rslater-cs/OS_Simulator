package FileHandler.Complier.Interpreter;

import FileHandler.Complier.Variables;
import ProcessFormats.Data.Opcode.ArgumentObjects.AddressMode;
import ProcessFormats.Data.Opcode.ArgumentObjects.Argument;
import ProcessFormats.Data.Opcode.Opcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Interpreter {
    private static final String RETURN_ADDR = "ret";
    private Variables variables;

    public Interpreter(Variables variables){
        this.variables = variables;
    }

    public ArrayList<Opcode> interpret(String codeLine, int lineNum){
        final Opcode ERROR_MESSAGE = new Opcode("err", new Argument[]{new Argument(Integer.toString(lineNum), AddressMode.IMMEDIATE)});
        ArrayList<Opcode> opcodes = new ArrayList<>();
        Stack<String> code = toStack(codeLine);
        while(code.size() > 1){
            Pack pack = new Pack(code, variables);
            if(pack.validate()){
                opcodes.add(pack.toOpcode());
                if(code.size() > 0){
                    code.add(RETURN_ADDR);
                }
            } else{
                opcodes.add(ERROR_MESSAGE);
            }
        }
        return opcodes;
    }

    private Stack<String> toStack(String codeLine){
        Stack<String> stack = new Stack<>();
        Collections.addAll(stack, codeLine.split(" "));
        return stack;
    }

}
