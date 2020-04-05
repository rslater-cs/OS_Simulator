import FileHandler.Complier.Compiler;
import FileHandler.FileReader;
import ProcessFormats.Opcode.Opcode;

import java.util.ArrayList;

public class Testing {

    public static void main(String[] args){
        Compiler compiler = new Compiler();
        FileReader fileReader = new FileReader("exampleCode.txt");
        ArrayList<Opcode> opcodes = compiler.compile(fileReader.getAll());

        for(Opcode opcode : opcodes){
            System.out.println(opcode);
        }
    }
}
