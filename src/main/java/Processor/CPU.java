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
    private int returnAddress;

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
            PCB pcb = readyQueue.remove();
            returnAddress = pcb.getReturnAddress() + pcb.getMemoryLimits().getEnd();
            int address = pcb.getProcessCounter();
            System.out.println(address);
            Instruction instruction = fetch(pcb.getID(), address);
            instruction = decode(instruction, pcb.getMemoryLimits().getEnd(), pcb.getID());
            execute(instruction, pcb.getID(), pcb.getMemoryLimits().getEnd());
            //clock();
            if(pcb.getProcessState() == ProcessState.TERMINATING){
                addressQueue.add(new Address(pcb.getID(), -1));
                pcb.setProcessState(ProcessState.TERMINATED);
            } else{
                jobQueue.add(pcb);
            }
        }
    }

    private Instruction fetch(int pid, int address){
        clock();
        addressQueue.add(new Address(pid, address));
        return dataToCPU.remove();
    }

    private Instruction decode(Instruction instruction, int instructionLength, int pid){
        System.out.println(instruction);

        int start = 0;
        if(instruction.getProcess().equals("str")) start = 1;

        for (int x = start; x < instruction.getArgNumber(); x++) {
            if (instruction.getArg(x).getAddressMode() == AddressMode.DIRECT) {
                instruction.setArg(x, fetch(pid, instructionLength + instruction.getArg(x).getIntArgument()).getArg(0));
            }
        }

        return instruction;
    }

    private void execute(Instruction instruction, int pid, int instructionLength){
        Operand result = switch(instruction.getProcess()){
            case STR -> str(pid, instructionLength, instruction);
            case ADD -> add(instruction.getArgs());
            case SUB -> sub(instruction.getArgs());
            case MUL -> mul(instruction.getArgs());
            case DIV -> div(instruction.getArgs());
            case OUT -> out(instruction.getArgs());
            default -> out(new Operand[]{new Operand("cpu err at process: '" + instruction.getProcess() + "'", AddressMode.IMMEDIATE)});
        };
        addressQueue.add(new Address(pid, returnAddress));
        dataToMemory.add(new Instruction(Opcode.DAT, new Operand[]{result}));
    }

    private Operand str(int pid, int instructionLength, Instruction instruction){
        addressQueue.add(new Address(pid, instruction.getArg(0).getIntArgument()+instructionLength));
        dataToMemory.add(new Instruction(Opcode.DAT, new Operand[]{instruction.getArg(1)}));
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
        System.out.println(args[0].getValue());
        printQueue.add(args[0].getValue());
        return new Operand("", AddressMode.IMMEDIATE);
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
