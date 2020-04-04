package FileHandler.Complier.Interpreter;

import ProcessFormats.Opcode.Opcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Interpreter {
    private static final String RETURN_ADDR = "ret";

    public ArrayList<Opcode> interpret(String codeLine, int lineNum){
        final Opcode ERROR_MESSAGE = new Opcode("err", new String[]{Integer.toString(lineNum)});
        ArrayList<Opcode> opcodes = new ArrayList<>();
        Stack<String> code = toStack(codeLine);
        while(code.size() > 1){
            Pack pack = new Pack(code);
            if(pack.isValid()){
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
