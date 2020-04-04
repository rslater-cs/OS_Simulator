package FileHandler.Complier;

import FileHandler.Complier.Interpreter.Interpreter;
import ProcessFormats.Opcode.Opcode;

import java.util.ArrayList;

public class Compiler {
    private static final Interpreter interpreter = new Interpreter();

    public ArrayList<Opcode> compile(String[] code){
        ArrayList<Opcode> opcodes = new ArrayList<>();
        for(int x = 0; x < code.length; x++){
            opcodes.addAll(interpreter.interpret(code[x], x));
        }
        return opcodes;
    }

}
