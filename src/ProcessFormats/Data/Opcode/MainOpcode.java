package ProcessFormats.Data.Opcode;

import ProcessFormats.Data.Opcode.Argument.Argument;

public abstract class MainOpcode {
    private Argument[] args;

    public MainOpcode(Argument[] args){
        if(args.length > 2) throw new IllegalArgumentException("Opcode can only have up to 2 arguements, " + args.length + " given");
        this.args = args;
    }

    public Argument getArg(int index){
        if(index < args.length && index >= 0){
            return args[index];
        }
        return null;
    }

    public int getIntArg(int index){
        if(getArg(index) != null){
            return getArg(index).getIntArgument();
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public String toString(){
        String result =  "";
        for(Argument arg : args){
            result += arg.toString() + " ";
        }
        return result;
    }
}
