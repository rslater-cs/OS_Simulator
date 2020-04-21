package FileHandler.Complier;

import FileHandler.Complier.Interpreter.Interpreter;
import ProcessFormats.Data.Opcode.ArgumentObjects.AddressMode;
import ProcessFormats.Data.Opcode.ArgumentObjects.Argument;
import ProcessFormats.Data.Opcode.Opcode;

import java.util.ArrayList;

public class Compiler {
    //private static final Interpreter interpreter = new Interpreter(new Variables());
    //private Variables variables = new Variables();

    public ArrayList<Opcode> compile(String[] code){
        Variables variables = new Variables();
        final Interpreter interpreter = new Interpreter(variables);
        ArrayList<Opcode> opcodes = new ArrayList<>();
        for(int x = 0; x < code.length; x++){
            opcodes.addAll(interpreter.interpret(code[x], x));
        }

        for(int x = 0; x < variables.size(); x++){
            opcodes.add(new Opcode("", new Argument[]{new Argument("", AddressMode.NONE)}));
        }

        System.out.println(variables);

        return opcodes;
    }
}
