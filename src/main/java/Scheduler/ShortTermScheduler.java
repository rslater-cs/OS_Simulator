package Scheduler;

import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.PCB;

public class ShortTermScheduler extends Thread{
    private SynchronisedArrayList<PCB> sortedJobs;
    private SynchronisedQueue<PCB> readyQueue;
    private boolean computerIsRunning = true;

    public ShortTermScheduler(SynchronisedArrayList<PCB> sortedJobs, SynchronisedQueue<PCB> readyQueue){
        this.sortedJobs = sortedJobs;
        this.readyQueue = readyQueue;
    }

    public void run(){
        while(computerIsRunning){
            final int leastExecuted = getLeastExecuted();
            if(leastExecuted > -1) {
                PCB pcb = sortedJobs.remove(leastExecuted);
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

    public void endThread(){
        computerIsRunning = false;
    }

}
