package ProcessFormats.Opcode;

import ProcessFormats.Process.ProcessControlBlock.ProcessPriority;

public class Opcode {
    private String process;
    private String[] args;

    public Opcode(String process, String[] args){
        if(process == null) throw new IllegalArgumentException("Null value passed to Opcode constructor");
        this.process = process;
        this.args = args;
    }

    public String getProcess(){
        return process;
    }

    public String[] getArgs(){
        return args;
    }
}
