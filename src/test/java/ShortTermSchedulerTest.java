import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.PCB;
import Scheduler.LongTermScheduler;
import Scheduler.ShortTermScheduler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortTermSchedulerTest {

    @Test
    public void executionTimeTest(){
        PCB pcb1 = new PCB(0, new MemoryLimits(0, 0, 0), ProcessPriority.HIGH);
        pcb1.setExecutionTime(1000);

        PCB pcb2 = new PCB(1, new MemoryLimits(0, 0, 0), ProcessPriority.HIGH);
        pcb2.setExecutionTime(3000);

        PCB pcb3 = new PCB(2, new MemoryLimits(0, 0, 0), ProcessPriority.HIGH);
        pcb3.setExecutionTime(1500);

        SynchronisedArrayList<PCB> sortedJobs = new SynchronisedArrayList<>(10);
        sortedJobs.add(pcb1);
        sortedJobs.add(pcb2);
        sortedJobs.add(pcb3);

        SynchronisedQueue<PCB> readyQueue = new SynchronisedQueue<>(10);
        SynchronisedQueue<PCB> deadQueue = new SynchronisedQueue<>(10);

        ShortTermScheduler scheduler = new ShortTermScheduler(sortedJobs, readyQueue, deadQueue);

        scheduler.start();

        assertEquals(pcb1, readyQueue.remove());
        assertEquals(pcb3, readyQueue.remove());
        assertEquals(pcb2, readyQueue.remove());
    }

    @Test
    public void executionTimeEqualTest(){
        PCB pcb1 = new PCB(0, new MemoryLimits(0, 0, 0), ProcessPriority.HIGH);
        pcb1.setExecutionTime(1000);

        PCB pcb2 = new PCB(1, new MemoryLimits(0, 0, 0), ProcessPriority.HIGH);
        pcb2.setExecutionTime(1000);

        SynchronisedArrayList<PCB> sortedJobs = new SynchronisedArrayList<>(10);
        sortedJobs.add(pcb1);
        sortedJobs.add(pcb2);

        SynchronisedQueue<PCB> readyQueue = new SynchronisedQueue<>(10);
        SynchronisedQueue<PCB> deadQueue = new SynchronisedQueue<>(10);

        ShortTermScheduler scheduler = new ShortTermScheduler(sortedJobs, readyQueue, deadQueue);

        scheduler.start();

        assertEquals(pcb1, readyQueue.remove());
        assertEquals(pcb2, readyQueue.remove());
    }
}
