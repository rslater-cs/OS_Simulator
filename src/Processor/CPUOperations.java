package Processor;

import java.util.Map;

public class CPUOperations {
    Map<String, Object> vars;
    private static final String[] operations = {"add", "sub", "mul", "div", "str", "ldr", "equ", "out", "inp"};

    public CPUOperations(Map<String, Object> vars){
        this.vars = vars;
    }

    public boolean isOperation(String operation){
        for(String op : operations){
            if(op.equals(operation)){
                return true;
            }
        }
        return false;
    }

    public Object add(String[] nums){
        final int[] intNums = toInts(nums);
        return intNums[0] + intNums[1];
    }

    public Object sub(String[] nums){
        final int[] intNums = toInts(nums);
        return intNums[0] - intNums[1];
    }

    public Object mul(String[] nums){
        int[] intNums = toInts(nums);
        return intNums[0] * intNums[1];
    }

    public Object div(String[] nums){
        int[] intNums = toInts(nums);
        return intNums[0] / intNums[1];
    }

    public boolean str(String[] args){
        args[1] = replaceSymbol(args[1]);
        if(vars.containsKey(args[0])){
            vars.replace(args[0], args[1]);
            return true;
        }
        vars.put(args[0], args[1]);
        return true;
    }

    public Object ldr(String[] args){
        if(vars.containsKey(args[0])){
            return vars.get(args[0]);
        }
        return null;
    }

    public boolean equ(String[] args){
        args = replaceSymbols(args);
        return args[0].equals(args[1]);
    }

    public Object out(String[] arg){
        Object ret = replaceSymbol(arg[0]);
        System.out.println(ret);
        return ret;
    }
    public void end(String[] args){
        System.exit(0);
    }

    public void err(String[] args){
        out(new String[]{"syntax error at high level found"});
        end(args);
    }

    private int[] toInts(String[] strNums){
        strNums = replaceSymbols(strNums);

        int[] intNums = new int[2];
        for(int x = 0; x < 2; x++){
            intNums[x] = Integer.parseInt(strNums[x]);
        }
        return intNums;
    }

    private String replaceSymbol(String arg){
        Object num = ldr(new String[]{arg});
        if(num != null){
            arg = num.toString();
        }
        return arg;
    }

    private String[] replaceSymbols(String[] args){
        for(int x = 0; x < 2; x++){
            args[x] = replaceSymbol(args[x]);
        }
        return args;
    }
}
