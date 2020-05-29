import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.PCB;
import Scheduler.LongTermScheduler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongTermSchedulerTest {

    @Test
    public void priorityHighTest(){
        PCB pcb = new PCB(0,new MemoryLimits(0, 0, 0), ProcessPriority.HIGH);

        SynchronisedQueue<PCB> jobQueue = new SynchronisedQueue<>(10);

        SynchronisedArrayList<PCB> sortedJobs = new SynchronisedArrayList<>(10);

        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, sortedJobs, 10, 5);

        jobQueue.add(pcb);

        longTermScheduler.start();

        PCB pcb2 = sortedJobs.remove(0);

        assertEquals(10, pcb2.getQuantum());
    }

    @Test
    public void priorityLowTest(){
        PCB pcb = new PCB(0,new MemoryLimits(0, 0, 0), ProcessPriority.LOW);

        SynchronisedQueue<PCB> jobQueue = new SynchronisedQueue<>(10);

        SynchronisedArrayList<PCB> sortedJobs = new SynchronisedArrayList<>(10);

        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, sortedJobs, 10, 5);

        jobQueue.add(pcb);

        longTermScheduler.start();

        PCB pcb2 = sortedJobs.remove(0);

        assertEquals(5, pcb2.getQuantum());
    }
}
