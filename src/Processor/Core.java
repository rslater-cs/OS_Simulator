package Processor;

import ProcessFormats.Opcode.Opcode;

import java.util.Queue;

public class Core {
    private CPU parent;
    private int coreFreq;
    private Queue<Opcode> coreInstructions;
    public Core(CPU parent, int coreFreq, Queue<Opcode> coreInstructions){
        this.parent = parent;
        this.coreFreq = coreFreq;
        this.coreInstructions = coreInstructions;
    }
}
