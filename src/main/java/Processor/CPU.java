package Processor;


import DataTypes.SynchronisedQueue;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Opcode.ArgumentObjects.AddressMode;
import ProcessFormats.Data.Opcode.ArgumentObjects.Argument;
import ProcessFormats.Data.Opcode.Opcode;
import ProcessFormats.ProcessControlBlock.PCB;

public class CPU extends Thread{
    private SynchronisedQueue<PCB> readyQueue;
    private SynchronisedQueue<PCB> jobQueue;
    private SynchronisedQueue<Address> addressQueue;
    private SynchronisedQueue<Opcode> dataToCPU;
    private SynchronisedQueue<Opcode> dataToMemory;
    private SynchronisedQueue<String> printQueue;
    private int freq;
    private boolean computerIsRunning;
    private int multiplier = 1;
    private int returnAddress;

    public CPU(SynchronisedQueue<PCB> readyQueue, SynchronisedQueue<PCB> jobQueue, SynchronisedQueue<Address> addressQueue,
               SynchronisedQueue<Opcode> dataToCPU, SynchronisedQueue<Opcode> dataToMemory, SynchronisedQueue<String> printQueue, int freq,
               boolean computerIsRunning){
        this.freq = freq;
        this.readyQueue = readyQueue;
        this.jobQueue = jobQueue;
        this.addressQueue = addressQueue;
        this.dataToCPU = dataToCPU;
        this.dataToMemory = dataToMemory;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){
            PCB pcb = readyQueue.remove();
            returnAddress = pcb.getReturnAddress() + pcb.getMemoryLimits().getEnd();
            Opcode opcode = fetch(pcb.getID(), pcb.getProcessCounter());
            decode(opcode, pcb.getMemoryLimits().getEnd(), pcb.getID());
        }
    }

    private Opcode fetch(int pid, int address){
        addressQueue.add(new Address(pid, address));
        return dataToCPU.remove();
    }

    private Opcode decode(Opcode opcode, int instructionLength, int pid){
        System.out.println(opcode);

        int start = 0;
        if(opcode.getProcess().equals("str")) start = 1;

        for (int x = start; x < opcode.getArgNumber(); x++) {
            if (opcode.getArg(x).getAddressMode() == AddressMode.DIRECT) {
                opcode.setArg(x, fetch(pid, instructionLength + opcode.getArg(x).getIntArgument()).getArg(0));
            }
        }

        System.out.println(opcode);

        return opcode;
    }

    private void execute(Opcode opcode, int pid, int instructionLength){
        Argument result = switch(opcode.getProcess()){
            case "str" -> str(pid, instructionLength, opcode);
            case "add" -> add(opcode.getArgs());
            case "sub" -> sub(opcode.getArgs());
            case "mul" -> mul(opcode.getArgs());
            case "div" -> div(opcode.getArgs());
            case "out" -> out(opcode.getArgs());
            default -> out(new Argument[]{new Argument("cpu err", AddressMode.NONE)});
        };
        addressQueue.add(new Address(pid, returnAddress));
        dataToMemory.add(new Opcode("", new Argument[]{result}));
    }

    private Argument str(int pid, int instructionLength, Opcode opcode){
        addressQueue.add(new Address(pid, opcode.getArg(0).getIntArgument()+instructionLength));
        dataToMemory.add(new Opcode("", new Argument[]{opcode.getArg(1)}));
        return new Argument(0, AddressMode.IMMEDIATE);
    }

    private Argument add(Argument[] args){
        return new Argument(args[0].getIntArgument() + args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Argument sub(Argument[] args){
        return new Argument(args[0].getIntArgument() - args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Argument mul(Argument[] args){
        return new Argument(args[0].getIntArgument() * args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Argument div(Argument[] args){
        return new Argument(args[0].getIntArgument() / args[1].getIntArgument(), AddressMode.IMMEDIATE);
    }

    private Argument out(Argument[] args){
        printQueue.add(args[0].getValue());
        return new Argument("", AddressMode.IMMEDIATE);
    }

    public void setMultiplier(int multiplier){
        this.multiplier = multiplier;
        System.out.println(multiplier);
    }

    private void clock(){
        try{
            Thread.sleep((long)(((1.0/freq)*1000)*multiplier));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
