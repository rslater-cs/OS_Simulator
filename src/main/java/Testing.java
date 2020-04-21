import FileHandler.Complier.Compiler;
import FileHandler.FileReader;
import ProcessFormats.Data.Opcode.Opcode;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;

import java.util.ArrayList;
import java.util.Random;

public class Testing {

    public static void main(String[] args){
        Random rand = new Random();
        Compiler compiler = new Compiler();
        FileReader fileReader = new FileReader("TestCodeOne.txt");
        fileReader.getLine();
        ProcessPriority priority = ProcessPriority.HIGH;
        ArrayList<Opcode> opcodes = compiler.compile(fileReader.getRest());

        for(Opcode opcode : opcodes){
            System.out.println(opcode);
        }

        //PCB pcb = new PCB(opcodes.size(), priority);

        //System.out.println(pcb);

    }
}
