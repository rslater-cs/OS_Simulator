import FileHandler.FileReader;
import ProcessFormats.Opcode.Opcode;
import Processor.CPU;

import java.util.LinkedList;
import java.util.Queue;

public class Computer {
    private static final String BOOT_PATH = "bootfile.txt";

    public static void main(String[] args){
        FileReader fileReader = new FileReader(BOOT_PATH);

        int[] bootInfo = new int[fileReader.getFileLength()];

        for(int x = 0; x < bootInfo.length; x++){
            bootInfo[x] = Integer.parseInt(fileReader.getLine().split(" ")[2]);
        }

        Queue<Opcode> fileRequests = new LinkedList<>();
        Queue<Opcode> processorInstructions = new LinkedList<>();
        Queue<Opcode> scheduleInstructions = new LinkedList<>();

        new CPU(processorInstructions, fileRequests, bootInfo[1]);
    }
}
