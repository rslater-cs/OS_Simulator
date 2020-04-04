package ProcessFormats.Process.ProcessControlBlock;

public class PCB {
    private ProcessState processState = ProcessState.NEW;
    private int processID = -1;
    private int processCounter;
    private int memoryLimit;
    private ProcessPriority priority;

    public PCB(int processCounter, int memoryLimit, ProcessPriority priority){
        this.processCounter = processCounter;
        this.memoryLimit = memoryLimit;
        this.priority = priority;
    }

    public void setID(int PID){
        processID = PID;
    }

    public int getID(){
        return processID;
    }

    public int getProcessCounter() {
        if(processCounter - memoryLimit == 0){
            this.processState = ProcessState.TERMINATING;
        }
        return processCounter++;
    }

    public void setProcessCounter(int process){
        processCounter = process;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public ProcessPriority getPriority() {
        return priority;
    }
}
