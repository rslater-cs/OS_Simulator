package Processor;


import DataTypes.SynchronisedQueue;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;
import ProcessFormats.ProcessControlBlock.PCB;

public class CPU extends Thread{
    private SynchronisedQueue<PCB> readyQueue;
    private SynchronisedQueue<PCB> jobQueue;

    private SynchronisedQueue<Address> addressFromCPUToMemory;
    private SynchronisedQueue<Address> addressFromCPUToCache;

    private SynchronisedQueue<Instruction> dataFromMemoryToCPU;
    private SynchronisedQueue<Instruction> dataFromCPUToMemory;
    private SynchronisedQueue<Instruction> dataFromCacheToCPU;

    private SynchronisedQueue<String> printQueue;

    private int freq;
    private boolean computerIsRunning = true;

    private Operand currentReturnRegister;
    private PCB currentPCB;

    public CPU(SynchronisedQueue<PCB> readyQueue,
               SynchronisedQueue<PCB> jobQueue,
               SynchronisedQueue<Address> addressFromCPUToMemory,
               SynchronisedQueue<Address> addressFromCPUToCache,
               SynchronisedQueue<Instruction> dataFromMemoryToCPU,
               SynchronisedQueue<Instruction> dataFromCPUToMemory,
               SynchronisedQueue<Instruction> dataFromCacheToCPU,
               SynchronisedQueue<String> printQueue,
               int freq){
        this.freq = freq;

        this.readyQueue = readyQueue;
        this.jobQueue = jobQueue;

        this.addressFromCPUToMemory = addressFromCPUToMemory;
        this.addressFromCPUToCache = addressFromCPUToCache;

        this.dataFromMemoryToCPU = dataFromMemoryToCPU;
        this.dataFromCPUToMemory = dataFromCPUToMemory;
        this.dataFromCacheToCPU = dataFromCacheToCPU;

        this.printQueue = printQueue;
    }

    public void run(){
        while(computerIsRunning) {
            if (readyQueue.size() > 0) {
                currentPCB = readyQueue.remove();
                currentReturnRegister = currentPCB.restoreRegister();
                for (int x = 0; x < currentPCB.getQuantum(); x++) {
                    int address = currentPCB.incProgramCounter();
                    Instruction instruction = instructionFetch(address);
                    System.out.println(instruction);
                    if(instruction != null) {
                        instruction = decode(instruction);
                        execute(instruction);
                    }else{
                        printQueue.add("Instruction received is null, aborting execution");
                        currentPCB.setProcessState(ProcessState.TERMINATING);
                    }
                    if (currentPCB.getProcessState() == ProcessState.TERMINATING) {
                        addressFromCPUToMemory.add(new Address(currentPCB.getID(), -1));
                        currentPCB.setProcessState(ProcessState.TERMINATED);
                        break;
                    }
                }
                if (currentPCB.getProcessState() != ProcessState.TERMINATED) {
                    currentPCB.pasteRegister(currentReturnRegister);
                    jobQueue.add(currentPCB);
                }
            }
        }
    }

    private Instruction instructionFetch(int address){
        addressFromCPUToCache.add(new Address(currentPCB.getID(), address));
        return dataFromCacheToCPU.remove();
    }

    private Instruction dataFetch(int address){
        addressFromCPUToMemory.add(new Address(currentPCB.getID(), address));
        clock();
        return dataFromMemoryToCPU.remove();
    }

    private Instruction decode(Instruction instruction){

        int start = 0;
        if(instruction.getProcess().equals(Opcode.STR)) start = 1;

        for (int x = start; x < instruction.getArgNumber(); x++) {
            if (instruction.getArg(x).getAddressMode() == AddressMode.DIRECT) {
                instruction.setArg(x, dataFetch(currentPCB.getMemoryLimits().getEnd() + instruction.getArg(x).getIntArgument()).getArg(0));
            }else if(instruction.getArg(x).getAddressMode() == AddressMode.REGISTER){
                instruction.setArg(x, new Operand(currentReturnRegister.getValue(), AddressMode.REGISTER));
            }
        }

        return instruction;
    }

    public void execute(Instruction instruction){
        Operand result = switch(instruction.getProcess()){
            case STR -> str(instruction.getArgs());
            case ADD -> add(instruction.getArgs());
            case SUB -> sub(instruction.getArgs());
            case MUL -> mul(instruction.getArgs());
            case DIV -> div(instruction.getArgs());
            case OUT -> out(instruction.getArgs());
            default -> out(new Operand[]{new Operand("cpu err at process: '" + instruction.getProcess() + "'", AddressMode.IMMEDIATE)});
        };
        currentReturnRegister = result;
    }

    private Operand str(Operand[] args){
        addressFromCPUToMemory.add(new Address(currentPCB.getID(), args[0].getIntArgument()+currentPCB.getMemoryLimits().getEnd()));
        dataFromCPUToMemory.add(new Instruction(Opcode.DAT, new Operand[]{args[1]}));
        return new Operand(0, AddressMode.IMMEDIATE);
    }

    private Operand add(Operand[] args){
        return new Operand(args[0].getIntArgument() + args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Operand sub(Operand[] args){
        return new Operand(args[0].getIntArgument() - args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Operand mul(Operand[] args){
        return new Operand(args[0].getIntArgument() * args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Operand div(Operand[] args){
        if(args[1].getIntArgument() == 0){
            return new Operand(0, AddressMode.IMMEDIATE);
        }
        return new Operand(args[0].getIntArgument() / args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Operand out(Operand[] args){
        printQueue.add("[Process " + currentPCB.getID() + " output] " + args[0].getValue());
        return new Operand("", AddressMode.IMMEDIATE);
    }

    public Operand getReturnRegister(){
        return currentReturnRegister;
    }

    public void testSetPCB(PCB pcb){
        currentPCB = pcb;
    }

    public void setFreq(int multiplier){
        this.freq = multiplier;
    }

    public void endThread(){
        computerIsRunning = false;
    }

    private void clock(){
        try{
            Thread.sleep((long)(((1.0/freq)*1000)));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
