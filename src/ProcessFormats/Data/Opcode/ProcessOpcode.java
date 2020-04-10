package ProcessFormats.Data.Opcode;

import ProcessFormats.Data.Opcode.Argument.Argument;

public class ProcessOpcode extends MainOpcode{
    private Argument[] args;
    private String process;

    public ProcessOpcode(Argument[] args, String process){
        super(args);
        this.process = process;
    }

    public String getProcess(){
        return process;
    }

    @Override
    public String toString(){
        String result =  process + " ";
        result += super.toString();
        return result;
    }
}
