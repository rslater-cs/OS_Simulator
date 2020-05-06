package FileHandler.Complier.Interpreter;

import FileHandler.Complier.Variables;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Interpreter {
    private static final String RETURN_ADDR = "ret";
    private Variables variables;

    public Interpreter(Variables variables){
        this.variables = variables;
    }

    public ArrayList<Instruction> interpret(String codeLine, int lineNum){
        final Instruction ERROR_MESSAGE = new Instruction(Opcode.ERR, new Operand[]{new Operand(Integer.toString(lineNum), AddressMode.IMMEDIATE)});
        ArrayList<Instruction> instructions = new ArrayList<>();
        Stack<String> code = toStack(codeLine);
        while(code.size() > 1){
            Pack pack = new Pack(code, variables);
            if(pack.validate()){
                instructions.add(pack.toOpcode());
                if(code.size() > 0){
                    code.add(RETURN_ADDR);
                }
            } else{
                instructions.add(ERROR_MESSAGE);
            }
        }
        return instructions;
    }

    private Stack<String> toStack(String codeLine){
        Stack<String> stack = new Stack<>();
        Collections.addAll(stack, codeLine.split(" "));
        return stack;
    }

}
