package ProcessFormats.Opcode;

import ProcessFormats.Process.ProcessControlBlock.ProcessPriority;

public class Opcode {
    private String process;
    private Argument[] args;

    public Opcode(String process, Argument[] args){
        if(process == null) throw new IllegalArgumentException("Null value passed to Opcode constructor");
        this.process = process;
        this.args = args;
    }

    public String getProcess(){
        return process;
    }

    public Argument[] getArgs(){
        return args;
    }

    public void setArg(int index, Argument value){
        args[index] = value;
    }

    @Override
    public String toString(){
        String result =  process + " ";
        for(Argument arg : args){
            result += arg.toString() + " ";
        }
        return result;
    }
}
