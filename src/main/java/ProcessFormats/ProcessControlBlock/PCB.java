package ProcessFormats.ProcessControlBlock;

import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessTime;

public class PCB {
    private ProcessState processState = ProcessState.NEW;
    private int processID = -1;
    private int processCounter = 1;
    private MemoryLimits memoryLimits;
    private ProcessPriority priority;
    private int quantum;
    private ProcessTime processTime = new ProcessTime();
    int returnAddress;

    public PCB(int pid, MemoryLimits memoryLimits, int returnAddress, ProcessPriority priority){
        this.processID = pid;
        this.memoryLimits = memoryLimits;
        this.priority = priority;
        this.returnAddress = returnAddress;
    }

    public int getID(){
        return processID;
    }

    public int getProcessCounter() {
        processTime.setEnd();
        if(processCounter+1 - memoryLimits.getEnd() == 0){
            this.processState = ProcessState.TERMINATING;
        }
        return processCounter++;
    }

    public void setProcessCounter(int process){
        processCounter = process;
    }

    public MemoryLimits getMemoryLimits() {
        return memoryLimits;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessState processState){
        if(processState == ProcessState.READY){
            processTime.setStart();
        } else if(processState == ProcessState.TERMINATING){
            processTime.setEnd();
        }
        this.processState = processState;
    }

    public ProcessPriority getPriority() {
        return priority;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public double getExecutionTime() {
        return processTime.totalTime();
    }

    public int getReturnAddress(){
        return returnAddress;
    }

    @Override
    public String toString(){
        return processID + ", " + priority + ", " + processState + ", " + processCounter + ", " + quantum + ", " + memoryLimits + ", " + getExecutionTime();
    }
}
