package ProcessFormats.ProcessControlBlock;

import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessTime;

public class PCB {
    private ProcessState processState = ProcessState.NEW;
    private int processID = -1;
    private int programCounter = 1;
    private MemoryLimits memoryLimits;
    private ProcessPriority priority;
    private int quantum;
    private ProcessTime processTime = new ProcessTime();
    private Operand returnRegister = new Operand(0, AddressMode.REGISTER);

    public PCB(int pid, MemoryLimits memoryLimits, ProcessPriority priority){
        this.processID = pid;
        this.memoryLimits = memoryLimits;
        this.priority = priority;
    }

    public int getID(){
        return processID;
    }

    public int getProgramCounter(){
        return programCounter;
    }


    //Make this cpu intensive?
    public int incProgramCounter() {
        processTime.setEnd();
        if(programCounter - memoryLimits.getEnd() == 0){
            this.processState = ProcessState.TERMINATING;
        }
        return programCounter++;
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

    public Operand restoreRegister(){
        return returnRegister;
    }

    public void pasteRegister(Operand returnRegister){
        this.returnRegister = returnRegister;
    }

    public void setExecutionTime(long millis){
        processTime.setTotalTime(millis);
    }

    @Override
    public String toString(){
        return processID + ", " + priority + ", " + processState + ", " + programCounter + ", " + quantum + ", " + memoryLimits + ", " + getExecutionTime();
    }
}
