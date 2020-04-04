package FileHandler;

import ProcessFormats.Opcode.Opcode;

import java.util.ArrayList;
import java.util.Queue;

public class FileHandler {
    private ArrayList<FileReader> files = new ArrayList<>();
    private Queue<Opcode> fileRequests;
    private Queue<Opcode> instructions;

    private static final String BOOT_INIT = "boot";
    private static final String CODE_INIT = ""

    public FileHandler(Queue<Opcode> fileRequests, Queue<Opcode> instructions){
        this.fileRequests = fileRequests;
        this.instructions = instructions;
    }

}
