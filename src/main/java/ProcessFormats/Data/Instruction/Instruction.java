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
    public boolean equals(Object obj) {
        if(obj == null) return false;
        final Instruction instruction = (Instruction)obj;
        if(args == null && instruction.args == null) return true;
        if(args == null ^ instruction.args == null) return false;
        if(instruction.process != process || instruction.args.length != args.length) return false;

        for(int x = 0; x < args.length; x++){
            if(!args[x].equals(instruction.getArg(x))) return false;
        }
        return true;
    }

    @Override
    public String toString(){
        String result =  process + " ";
        if(args == null) return result;
        for(Operand arg : args){
            if(arg != null) result += arg.toString() + " ";
        }
        return result;
    }
}
