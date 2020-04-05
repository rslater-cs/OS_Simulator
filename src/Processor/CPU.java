package Processor;


import ProcessFormats.Opcode.Opcode;
import ProcessFormats.Process.ProcessControlBlock.PCB;
import DataTypes.SynchronisedQueue;

public class CPU extends Thread{
    private SynchronisedQueue<PCB> readyQueue;
    private SynchronisedQueue<Integer> addressRequestQueue;
    private SynchronisedQueue<Opcode> addressReceiveQueue;
    private int freq;
    private boolean computerIsRunning;
    //private CPUOperations cpuOperations = new CPUOperations();

    public CPU(SynchronisedQueue<PCB> readyQueue, SynchronisedQueue<Integer> addressRequestQueue,
               SynchronisedQueue<Opcode> addressReceiveQueue, int freq, boolean computerIsRunning){
        this.freq = freq;
        this.readyQueue = readyQueue;
        this.addressRequestQueue = addressRequestQueue;
        this.addressReceiveQueue = addressReceiveQueue;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){
            addressRequestQueue.add(readyQueue.remove().getProcessCounter());
            Opcode opcode = addressReceiveQueue.remove();
        }
    }
}
