package Processor;

import FileHandler.FileHandler;
import ProcessFormats.Opcode.Opcode;
import Schedulers.RoundRobin.RoundRobin;

import java.util.*;

public class CPU extends Thread{
    private ArrayList<Queue<Opcode>> coreInstructions = new ArrayList<>();
    private Map<String, Object> variables = new HashMap<>();
    private CPUOperations operations = new CPUOperations(variables);
    private Queue<Opcode> processorInstructions;
    private Queue<Opcode> fileRequests;
    private int freq;
    private boolean running = true;

    public CPU(Queue<Opcode> processorInstructions, Queue<Opcode> fileRequests, int coreCount, int freq){
        this.processorInstructions = processorInstructions;
        this.fileRequests = fileRequests;
        createCores(coreCount);
        this.freq = freq;
    }

    private void createCores(int coreCount){
        for(int x = 0; x < coreCount; x++){
            Queue<Opcode> coreInstruction = new LinkedList<>();
            coreInstructions.add(coreInstruction);
            new Core(this, freq/coreCount, coreInstruction);
        }
    }

    //@Override
    public void run(){
        while(running){
            while(processorInstructions.size() == 0){
                //wait
            }
            while(processorInstructions.size() > 0){
                int
                coreInstructions.get(getMostAvailableCore()).add(processorInstructions.remove());
            }
        }
    }

    public void endCPU(){
        running = false;
    }

    private int getMostAvailableCore(){
        int bestCore = -1;
        while(bestCore == -1){
            bestCore = mostAvailableCore();
        }
        return bestCore;
    }

    private int mostAvailableCore(){
        int min = 5;
        int bestCore = -1;
        for(int x = 0; x < coreInstructions.size(); x++){
            final int size = coreInstructions.get(x).size();
            if(size < min){
                bestCore = x;
                min = size;
            }
        }
        return bestCore;
    }

}
