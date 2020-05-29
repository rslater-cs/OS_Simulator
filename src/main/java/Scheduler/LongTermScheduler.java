package Scheduler;

import datatypes.SynchronisedArrayList;
import datatypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.PCB;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;

public class LongTermScheduler extends Thread{
    private SynchronisedQueue<PCB> jobQueue;
    private SynchronisedArrayList<PCB> sortedJobs;
    boolean computerIsRunning = true;
    private int highQuantum;
    private int lowQuantum;

    public LongTermScheduler(SynchronisedQueue<PCB> jobQueue, SynchronisedArrayList<PCB> sortedJobs, int highQuantum, int lowQuantum){
        this.jobQueue = jobQueue;
        this.sortedJobs = sortedJobs;
        this.highQuantum = highQuantum;
        this.lowQuantum = lowQuantum;
    }

    public void run(){
        while(computerIsRunning){
            if(jobQueue.size() > 0) {
                final PCB process = jobQueue.remove();
                if (process.getPriority() == ProcessPriority.HIGH) {
                    process.setQuantum(highQuantum);
                } else if (process.getPriority() == ProcessPriority.LOW) {
                    process.setQuantum(lowQuantum);
                }
                sortedJobs.add(process);
            }
        }
    }

    public void endThread(){
        computerIsRunning = false;
    }
}
