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
    private SynchronisedQueue<Address> addressQueue;
    private SynchronisedQueue<Instruction> dataToCPU;
    private SynchronisedQueue<Instruction> dataToMemory;
    private SynchronisedQueue<String> printQueue;
    private int freq;
    private boolean computerIsRunning;
    private int multiplier = 1;
    private Operand currentReturnRegister;
    private PCB currentPCB;

    public CPU(SynchronisedQueue<PCB> readyQueue, SynchronisedQueue<PCB> jobQueue, SynchronisedQueue<Address> addressQueue,
               SynchronisedQueue<Instruction> dataToCPU, SynchronisedQueue<Instruction> dataToMemory, SynchronisedQueue<String> printQueue, int freq,
               boolean computerIsRunning){
        this.freq = freq;
        this.readyQueue = readyQueue;
        this.jobQueue = jobQueue;
        this.addressQueue = addressQueue;
        this.dataToCPU = dataToCPU;
        this.dataToMemory = dataToMemory;
        this.printQueue = printQueue;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){
            currentPCB = readyQueue.remove();
            //System.out.println("-------------------------");
            //System.out.println(currentPCB.getID());
            currentReturnRegister = currentPCB.restoreRegister();
            for(int x = 0; x < currentPCB.getQuantum(); x++) {
                int address = currentPCB.incProgramCounter();
                Instruction instruction = fetch(address);
                //System.out.println(instruction);
                instruction = decode(instruction);
                execute(instruction);
                if (currentPCB.getProcessState() == ProcessState.TERMINATING) {
                    addressQueue.add(new Address(currentPCB.getID(), -1));
                    currentPCB.setProcessState(ProcessState.TERMINATED);
                    break;
                }
            }
            if(currentPCB.getProcessState() != ProcessState.TERMINATED){
                currentPCB.pasteRegister(currentReturnRegister);
                jobQueue.add(currentPCB);
            }
        }
    }

    private Instruction fetch(int address){
        clock();
        addressQueue.add(new Address(currentPCB.getID(), address));
        return dataToCPU.remove();
    }

    private Instruction decode(Instruction instruction){

        int start = 0;
        if(instruction.getProcess().equals(Opcode.STR)) start = 1;

        for (int x = start; x < instruction.getArgNumber(); x++) {
            if (instruction.getArg(x).getAddressMode() == AddressMode.DIRECT) {
                instruction.setArg(x, fetch(currentPCB.getMemoryLimits().getEnd() + instruction.getArg(x).getIntArgument()).getArg(0));
            }else if(instruction.getArg(x).getAddressMode() == AddressMode.REGISTER){
                instruction.setArg(x, new Operand(currentReturnRegister.getValue(), AddressMode.REGISTER));
            }
        }

        return instruction;
    }

    private void execute(Instruction instruction){
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
        addressQueue.add(new Address(currentPCB.getID(), args[0].getIntArgument()+currentPCB.getMemoryLimits().getEnd()));
        dataToMemory.add(new Instruction(Opcode.DAT, new Operand[]{args[1]}));
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

    public void setMultiplier(int multiplier){
        this.multiplier = multiplier;
    }

    private void clock(){
        try{
            Thread.sleep((long)(((1.0/freq)*1000)*multiplier));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
