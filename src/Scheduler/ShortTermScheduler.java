package Scheduler;

import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import ProcessFormats.Process.ProcessControlBlock.PCB;

public class ShortTermScheduler extends Thread{
    private SynchronisedArrayList<PCB> sortedJobs;
    private SynchronisedQueue<PCB> readyQueue;
    private int maxSize;
    private boolean computerIsRunning;

    public ShortTermScheduler(SynchronisedArrayList<PCB> sortedJobs, SynchronisedQueue<PCB> readyQueue, int maxSize, boolean computerIsRunning){
        this.sortedJobs = sortedJobs;
        this.readyQueue = readyQueue;
        this.maxSize = maxSize;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){
            readyQueue.add(sortedJobs.remove(getLeastExecuted()));
        }
    }

    private int getLeastExecuted(){
        int smallestExecution = Integer.MAX_VALUE;
        int smallestExecutionIndex = -1;
        for(int x = 0; x < sortedJobs.size(); x++){
            final int executionTime = sortedJobs.get(x).getExecutionTime();
            if(executionTime < smallestExecution){
                smallestExecution = executionTime;
                smallestExecutionIndex = x;
            }
        }
        return smallestExecutionIndex;
    }
}
