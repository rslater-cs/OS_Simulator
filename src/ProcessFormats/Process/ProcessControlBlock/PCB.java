package ProcessFormats.Process.ProcessControlBlock;

public class PCB {
    private ProcessState processState = ProcessState.NEW;
    private int processID = -1;
    private int processCounter;
    private MemoryLimits memoryLimits;
    private ProcessPriority priority;

    public PCB(int processCounter, MemoryLimits memoryLimits, ProcessPriority priority){
        this.processCounter = processCounter;
        this.memoryLimits = memoryLimits;
        this.priority = priority;
    }

    public void setID(int PID){
        processID = PID;
    }

    public int getID(){
        return processID;
    }

    public int getProcessCounter() {
        if(processCounter - memoryLimits.getEnd() == 0){
            this.processState = ProcessState.TERMINATING;
        }
        return processCounter++;
    }

    public void setProcessCounter(int process){
        processCounter = process;
    }

    public int getMemoryEnd() {
        return memoryLimits.getEnd();
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public ProcessPriority getPriority() {
        return priority;
    }

    public int getMemoryStart() {
        return memoryLimits.getStart();
    }

    public int getProgramSize(){
        return memoryLimits.getSize();
    }
}
