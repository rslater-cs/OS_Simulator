package ProcessFormats.ProcessControlBlock;

import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessTime;

public class PCB {
    private ProcessState processState = ProcessState.NEW;
    private int processID = -1;
    private int processCounter = 0;
    private int memorySize;
    private ProcessPriority priority;
    private int quantum;
    private ProcessTime processTime = new ProcessTime();

    public PCB(int pid, int memorySize, ProcessPriority priority){
        this.processID = pid;
        this.memorySize = memorySize;
        this.priority = priority;
    }

    public int getID(){
        return processID;
    }

    public int getProcessCounter() {
        processTime.setEnd();
        if(processCounter+1 - memorySize == 0){
            this.processState = ProcessState.TERMINATING;
        }
        return processCounter++;
    }

    public void setProcessCounter(int process){
        processCounter = process;
    }

    public int getMemoryEnd() {
        return memorySize;
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

    @Override
    public String toString(){
        return processID + ", " + priority + ", " + processState + ", " + processCounter + ", " + quantum + ", " + memorySize + ", " + getExecutionTime();
    }
}
