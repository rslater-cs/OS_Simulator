package Scheduler;

import datatypes.SynchronisedArrayList;
import datatypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.PCB;

public class ShortTermScheduler extends Thread{
    private SynchronisedArrayList<PCB> sortedJobs;
    private SynchronisedQueue<PCB> readyQueue;
    private SynchronisedQueue<PCB> jobsToBeRun;

    private boolean computerIsRunning = true;

    public ShortTermScheduler(SynchronisedArrayList<PCB> sortedJobs,
                              SynchronisedQueue<PCB> readyQueue,
                              SynchronisedQueue<PCB> jobsToBeRun){
        this.sortedJobs = sortedJobs;
        this.readyQueue = readyQueue;
        this.jobsToBeRun = jobsToBeRun;
    }

    public void run(){
        while(computerIsRunning){
            final int leastExecuted = getLeastExecuted();
            if(leastExecuted > -1) {
                PCB pcb = sortedJobs.remove(leastExecuted);
                jobsToBeRun.add(pcb);
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
