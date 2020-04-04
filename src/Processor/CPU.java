package Processor;


import ProcessFormats.Process.ProcessControlBlock.PCB;
import DataTypes.SynchronisedQueue;

public class CPU extends Thread{
    private SynchronisedQueue<PCB> readyQueue;
    private int freq;
    private boolean computerIsRunning = false;

    public CPU(SynchronisedQueue<PCB> readyQueue, int freq, boolean computerIsRunning){
        this.freq = freq;
        this.readyQueue = readyQueue;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){

        }
    }
}
