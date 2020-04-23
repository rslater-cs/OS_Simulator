package Shell.CommandExecuter;

import DataTypes.BiDirectionalQueue;
import DataTypes.SynchronisedQueue;
import FileHandler.Complier.Compiler;
import FileHandler.FileReader;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Opcode.ArgumentObjects.AddressMode;
import ProcessFormats.Data.Opcode.ArgumentObjects.Argument;
import ProcessFormats.Data.Opcode.Opcode;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import Shell.Text.Validater.Functions;
import Shell.Text.Validater.Validation;
import Shell.Text.userLine.LetterType;

import java.util.ArrayList;

public class Executer {
    private CPU processor;
    private Compiler compiler = new Compiler();
    private SynchronisedQueue<Address> addressQueue;
    private BiDirectionalQueue<Opcode> dataQueue;
    private SynchronisedQueue<PCB> jobQueue;
    private ProcessIDAssigner pidAssigner = new ProcessIDAssigner();

    public Executer(CPU processor, SynchronisedQueue<Address> addressQueue, BiDirectionalQueue<Opcode> dataQueue, SynchronisedQueue<PCB> jobQueue){
        this.processor = processor;
        this.addressQueue = addressQueue;
        this.dataQueue = dataQueue;
        this.jobQueue = jobQueue;
    }

    public Exception start(String line){
        String[] commands = line.split(" & ");
        Exception exception = null;
        for(String command : commands){
            Exception temp = execute(command);
            if(temp != null){
                exception = temp;
            }
        }
        return exception;
    }

    private Exception execute(String command){
        Object exception = null;
        String[] splitCommand = command.split(" ");

        if(splitCommand.length < 3 && splitCommand.length > 0){
            if(Functions.isFunction(splitCommand[0])){
                try {
                    exception = this.getClass().getMethod(splitCommand[0], String[].class).invoke(this, new Object[]{splitCommand});
                }catch(Exception ignore){
                    ignore.printStackTrace();
                }
            } else{
                return new Exception("'" + splitCommand[0] + "' is not a known command");
            }
        }
        return (Exception)exception;
    }

    public Exception exit(String[] command){
        if(command.length != 1){
            return wrongArgAmountException(0,  command.length-1, command[0]);
        }
        System.exit(0);
        return null;
    }

    public Exception run(String[] command){
        if(command.length != 2) return wrongArgAmountException(1, command.length-1, command[0]);
        else{
            if(Validation.validateWord(command[1]) != LetterType.DIRECTORY) return wrongArgTypeException();
        }

        FileReader file = null;

        try {
            file = new FileReader(command[1]);
        }catch(Exception e){
            return e;
        }

        ProcessPriority priority = getPriority(file.getLine());

        if(priority == null) return new Exception("Code file does not correctly specify program priority");

        ArrayList<Opcode> opcodes = compiler.compile(file.getRest());

        int returnAddress = opcodes.remove(0).getArg(0).getIntArgument();
        System.out.println(returnAddress);
        opcodes.add(0, new Opcode("header", new Argument[]{new Argument(Integer.toString(opcodes.size()+1), AddressMode.NONE)}));

        final int size = opcodes.size();

        final int pid = pidAssigner.getPID();

        for(int x = 0; x < opcodes.size(); x++){
            addressQueue.add(new Address(pid, x));
            dataQueue.send(opcodes.get(x));
        }

        PCB pcb = new PCB(pid, new MemoryLimits(1, size, opcodes.size()), returnAddress, priority);

        System.out.println(pcb);

        return null;
    }

    public Exception slowspeed(String[] command){
        if(command.length != 2) return wrongArgAmountException(1,  command.length-1, command[0]);
        else{
            if(Validation.validateWord(command[1]) != LetterType.VALUE) return wrongArgTypeException();
        }

        processor.setMultiplier(Integer.parseInt(command[1]));

        return null;
    }

    private Exception wrongArgAmountException(int target, int result, String method){
        return new Exception("Command '" + method + "' Takes " + target + " Arguments, " + result + " given");
    }

    private Exception wrongArgTypeException(){
        return new Exception("Incorrect datatype(s) passed");
    }

    private ProcessPriority getPriority(String priorityLine){
        final String[] prioritySegments = priorityLine.split(" ");
        if(prioritySegments.length != 3) return null;
        return switch (prioritySegments[2]){
            case "high" -> ProcessPriority.HIGH;
            case "low" -> ProcessPriority.LOW;
            default -> null;
        };
    }
}
