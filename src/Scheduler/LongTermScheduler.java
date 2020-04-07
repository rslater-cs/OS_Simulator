package Scheduler;

import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import ProcessFormats.Process.ProcessControlBlock.PCB;
import ProcessFormats.Process.ProcessControlBlock.ProcessPriority;

import java.util.ArrayList;

public class LongTermScheduler extends Thread{
    private SynchronisedQueue<PCB> jobQueue;
    private SynchronisedArrayList<PCB> sortedJobs;
    private int maxSize;
    boolean computerIsRunning;
    private int highQuantum;
    private int lowQuantum;

    public LongTermScheduler(SynchronisedQueue<PCB> jobQueue, SynchronisedArrayList<PCB> sortedJobs, int size, boolean computerIsRunning, int highQuantum, int lowQuantum){
        this.jobQueue = jobQueue;
        this.sortedJobs = sortedJobs;
        this.maxSize = size;
        this.computerIsRunning = computerIsRunning;
        this.highQuantum = highQuantum;
        this.lowQuantum = lowQuantum;
    }

    public void run(){
        while(computerIsRunning){
            final PCB process = jobQueue.remove();
            if(process.getPriority() == ProcessPriority.HIGH){
                process.setQuantum(highQuantum);
            } else if(process.getPriority() == ProcessPriority.LOW){
                process.setQuantum(lowQuantum);
            }
            sortedJobs.add(process);
        }
        while(jobQueue.size() > 0){
            jobQueue.remove();
        }
    }
}
