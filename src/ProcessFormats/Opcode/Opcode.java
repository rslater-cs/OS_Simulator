package ProcessFormats.Opcode;

public class Opcode {
    private String process;
    private String[] args;
    private int PID;

    public Opcode(int PID, String process, String[] args){
        if(process == null || args == null) throw new IllegalArgumentException("Null value passed to Opcode constructor");
        this.PID = PID;
        this.process = process;
        this.args = args;
    }

    public int getID(){
        return PID;
    }

    public String getProcess(){
        return process;
    }

    public String[] getArgs(){
        return args;
    }
}
