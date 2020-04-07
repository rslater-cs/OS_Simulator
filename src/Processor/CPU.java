package Processor;


import ProcessFormats.Opcode.Opcode;
import ProcessFormats.ProcessControlBlock.PCB;
import DataTypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessState;

public class CPU extends Thread{
    private SynchronisedQueue<PCB> readyQueue;
    private SynchronisedQueue<PCB> jobQueue;
    private SynchronisedQueue<Integer> addressRequestQueue;
    private SynchronisedQueue<Opcode> addressReceiveQueue;
    private int freq;
    private boolean computerIsRunning;
    private int multiplier = 1;
    //private CPUOperations cpuOperations = new CPUOperations();

    public CPU(SynchronisedQueue<PCB> readyQueue, SynchronisedQueue<PCB> jobQueue, SynchronisedQueue<Integer> addressRequestQueue,
               SynchronisedQueue<Opcode> addressReceiveQueue, int freq, boolean computerIsRunning){
        this.freq = freq;
        this.readyQueue = readyQueue;
        this.jobQueue = jobQueue;
        this.addressRequestQueue = addressRequestQueue;
        this.addressReceiveQueue = addressReceiveQueue;
        this.computerIsRunning = computerIsRunning;
    }

    public void run(){
        while(computerIsRunning){
            PCB pcb = readyQueue.remove();
            pcb.setProcessState(ProcessState.RUNNING);
            for(int x = 0; x < pcb.getQuantum(); x++){
                System.out.println(pcb);
                clock();
                pcb.getProcessCounter();
                if(pcb.getProcessState() == ProcessState.TERMINATING){
                    System.out.println("finishing execution time:");
                    System.out.println(pcb.getExecutionTime());
                    break;
                }
            }
            System.out.println("quantum break");
            if(pcb.getProcessState() != ProcessState.TERMINATING){
                pcb.setProcessState(ProcessState.WAITING);
                jobQueue.add(pcb);
            } else{

            }
            //addressRequestQueue.add(readyQueue.remove().getProcessCounter());
            //Opcode opcode = addressReceiveQueue.remove();
        }
    }

    public void setMultiplier(int multiplier){
        this.multiplier = multiplier;
    }

    private void clock(){
        try{
            Thread.sleep((long)(((1.0/freq)*1000)*multiplier));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
