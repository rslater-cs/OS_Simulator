package FileHandler.Complier;

import FileHandler.Complier.Interpreter.Interpreter;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.Instruction.Instruction;

import java.util.ArrayList;

public class Compiler {

    public ArrayList<Instruction> compile(String[] code){
        Variables variables = new Variables();
        final Interpreter interpreter = new Interpreter(variables);
        ArrayList<Instruction> instructions = new ArrayList<>();
        for(int x = 0; x < code.length; x++){
            instructions.addAll(interpreter.interpret(code[x], x));
        }

        instructions.add(0, new Instruction(Opcode.DAT, new Operand[]{new Operand(instructions.size(), AddressMode.NONE)}));

        for(int x = 0; x < variables.size(); x++){
            instructions.add(new Instruction(Opcode.DAT, new Operand[]{new Operand("", AddressMode.NONE)}));
        }

        return instructions;
    }
}
