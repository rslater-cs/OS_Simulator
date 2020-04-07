package Scheduler;

import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.PCB;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;

public class ShortTermScheduler extends Thread{
    private SynchronisedArrayList<PCB> sortedJobs;
    private SynchronisedQueue<PCB> readyQueue;
    private boolean computerIsRunning;
    private int pid = 0;

    public ShortTermScheduler(SynchronisedArrayList<PCB> sortedJobs, SynchronisedQueue<PCB> readyQueue, boolean computerIsRunning){
        this.sortedJobs = sortedJobs;
        this.readyQueue = readyQueue;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){
            final int leastExecuted = getLeastExecuted();
            if(leastExecuted > -1) {
                PCB pcb = sortedJobs.remove(leastExecuted);
                if(pcb.getProcessState() == ProcessState.NEW){
                    pcb.setProcessState(ProcessState.READY);
                    pcb.setID(pid++);
                }
                readyQueue.add(pcb);
            }
        }
    }

    private int getLeastExecuted(){
        double smallestExecution = Double.MAX_VALUE;
        int smallestExecutionIndex = -1;
        for(int x = 0; x < sortedJobs.size(); x++){
            final double executionTime = sortedJobs.get(x).getExecutionTime();
            if(executionTime < smallestExecution){
                smallestExecution = executionTime;
                smallestExecutionIndex = x;
            }
        }
        return smallestExecutionIndex;
    }

}
