package Shell.CommandExecuter;

import DataTypes.SynchronisedQueue;
import FileHandler.Complier.Compiler;
import FileHandler.FileReader;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import Shell.Text.Validater.Functions;
import Shell.Text.Validater.Validation;
import Shell.Text.userLine.LetterType;
import Shell.subsystemstats.SubSystemGraph;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Executer {
    private CPU processor;
    private Compiler compiler = new Compiler();
    private SynchronisedQueue<Address> addressQueue;
    private SynchronisedQueue<Instruction> dataQueue;
    private SynchronisedQueue<PCB> jobQueue;
    private ProcessIDAssigner pidAssigner = new ProcessIDAssigner();
    private ThreadExecutioner threadExecutioner;
    private SubSystemGraph grpah;

    public Executer(CPU processor,
                    SynchronisedQueue<Address> addressQueue,
                    SynchronisedQueue<Instruction> dataQueue,
                    SynchronisedQueue<PCB> jobQueue,
                    SubSystemGraph graph,
                    ThreadExecutioner threadExecutioner){
        this.processor = processor;
        this.addressQueue = addressQueue;
        this.dataQueue = dataQueue;
        this.jobQueue = jobQueue;
        this.threadExecutioner = threadExecutioner;
        this.grpah = graph;
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
        threadExecutioner.endProgram();
        return null;
    }

    public Exception run(String[] command){
        if(command.length != 2) return wrongArgAmountException(1, command.length-1, command[0]);
        else{
            if(Validation.validateWord(command[1]) != LetterType.DIRECTORY) return wrongArgTypeException();
        }

        FileReader file;

        try {
            file = new FileReader(command[1]);
        }catch(Exception e){
            return e;
        }

        ProcessPriority priority = getPriority(file.getLine());

        if(priority == null) return new Exception("Code file does not correctly specify program priority");

        ArrayList<Instruction> instructions = compiler.compile(file.getRest());

        final int size = instructions.remove(0).getArg(0).getIntArgument();
        instructions.add(0, new Instruction(Opcode.HDR, new Operand[]{new Operand(Integer.toString(instructions.size()+1), AddressMode.IMMEDIATE)}));

        final int pid = pidAssigner.getPID();

        for(int x = 0; x < instructions.size(); x++){
            addressQueue.add(new Address(pid, x));
            dataQueue.add(instructions.get(x));
        }

        PCB pcb = new PCB(pid, new MemoryLimits(1, size, instructions.size()), priority);

        jobQueue.add(pcb);

        return null;
    }

    public Exception setfreq(String[] command){
        if(command.length != 2) return wrongArgAmountException(1,  command.length-1, command[0]);
        else{
            if(Validation.validateWord(command[1]) != LetterType.VALUE) return wrongArgTypeException();
        }

        processor.setFreq(Integer.parseInt(command[1]));

        return null;
    }

    private Exception memstats(String[] command, String title){
        if(command.length > 1) return wrongArgAmountException(0, command.length-1, command[0]);
        Stage stage = new Stage();
        Scene scene = new Scene(grpah.render("Memory Usage"));
        stage.setScene(scene);
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
