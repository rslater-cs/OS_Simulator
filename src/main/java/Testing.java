import FileHandler.Complier.Compiler;
import FileHandler.FileReader;
import ProcessFormats.Data.Instruction.Instruction;
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
        ArrayList<Instruction> instructions = compiler.compile(fileReader.getRest());

        for(Instruction instruction : instructions){
            System.out.println(instruction);
        }

        //PCB pcb = new PCB(opcodes.size(), priority);

        //System.out.println(pcb);

    }
}
