package Shell.CommandExecuter;

public class ProcessIDAssigner {
    private int currentPID = 0;

    public int getPID(){
        return currentPID++;
    }
}
