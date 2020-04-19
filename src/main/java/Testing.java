import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import FileHandler.Complier.Compiler;
import FileHandler.FileReader;
import Memory.MemoryChip;
import ProcessFormats.Data.Opcode.Opcode;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import Scheduler.LongTermScheduler;
import Scheduler.ShortTermScheduler;

import java.util.ArrayList;
import java.util.Random;

public class Testing {

    public static void main(String[] args){
        Random rand = new Random();
        Compiler compiler = new Compiler();
        FileReader fileReader = new FileReader("TestCodeOne.txt");
        ArrayList<Opcode> opcodes = compiler.compile(fileReader.getAll());

        SynchronisedQueue<PCB> jobQueue = new SynchronisedQueue<>(Integer.MAX_VALUE);
        SynchronisedArrayList<PCB> sortedJobs = new SynchronisedArrayList<>(25);
        SynchronisedQueue<PCB> readyQueue = new SynchronisedQueue<>(5);

        boolean computerIsRunning = true;

        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, sortedJobs, computerIsRunning, 10, 5);
        ShortTermScheduler shortTermScheduler = new ShortTermScheduler(sortedJobs, readyQueue, computerIsRunning);

        CPU cpu = new CPU(readyQueue, jobQueue, null, null, 100, computerIsRunning);

        longTermScheduler.start();
        shortTermScheduler.start();
        cpu.start();

        /*int pointer = 0;

        for(int x = 0; x < 20; x++){
            int size = rand.nextInt(100)+1;
            int priorityNum = rand.nextInt(2);
            ProcessPriority priority = ProcessPriority.LOW;
            if(priorityNum == 1){
                priority = ProcessPriority.HIGH;
            }
            jobQueue.add(new PCB(new MemoryLimits(pointer, pointer+size, size), priority));
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        try{
            Thread.sleep(10000);
        }catch(Exception e){
            e.printStackTrace();
        }

        jobQueue.add(new PCB(new MemoryLimits(pointer, pointer+5, 5), ProcessPriority.LOW));*/

        MemoryChip memoryChip = new MemoryChip(144);
    }
}
