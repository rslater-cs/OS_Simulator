package ProcessFormats.ProcessControlBlock;

import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessTime;

public class PCB {
    private ProcessState processState = ProcessState.NEW;
    private int processID = -1;
    private int processCounter = 0;
    private MemoryLimits memoryLimits;
    private ProcessPriority priority;
    private int quantum;
    private ProcessTime processTime = new ProcessTime();

    public PCB(MemoryLimits memoryLimits, ProcessPriority priority){
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
        processTime.setEnd();
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

    public int getMemoryStart() {
        return memoryLimits.getStart();
    }

    public int getProgramSize(){
        return memoryLimits.getSize();
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
        return processID + ", " + priority + ", " + processState + ", " + processCounter + ", " + quantum + ", " + getExecutionTime();
    }
}
