package ProcessFormats.Data.Instruction;

import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.Operand;

public class Instruction {
    private Opcode process;
    private Operand[] args;

    public Instruction(Opcode process, Operand[] args){
        if(process == null) throw new IllegalArgumentException("Null value passed to Opcode constructor");
        this.process = process;
        this.args = args;
    }

    public Opcode getProcess(){
        return process;
    }

    public Operand getArg(int index){
        if(index < args.length) {
            return args[index];
        }
        return null;
    }

    public Operand[] getArgs(){
        return args;
    }

    public int getArgNumber(){
        return args.length;
    }

    public void setArg(int index, Operand value){
        args[index] = value;
    }

    @Override
    public String toString(){
        String result =  process + " ";
        for(Operand arg : args){
            result += arg.toString() + " ";
        }
        return result;
    }
}
