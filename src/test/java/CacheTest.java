import datatypes.SynchronisedQueue;
import Memory.cache.Cache;
import Memory.ram.MemoryController;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.PCB;
import Shell.subsystemstats.GraphData;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CacheTest {

    @Test
    public void cachePreLoadedTest(){
        SynchronisedQueue<Instruction> dataFromCPUToMemory = new SynchronisedQueue<>(10);
        SynchronisedQueue<Instruction> dataFromMemoryToCache = new SynchronisedQueue<>(10);
        SynchronisedQueue<Instruction> dataFromCacheToCPU = new SynchronisedQueue<>(10);

        SynchronisedQueue<Address> addressFromCPUToCache =  new SynchronisedQueue<>(10);
        SynchronisedQueue<Address> addressFromCacheToMemory = new SynchronisedQueue<>(10);
        SynchronisedQueue<Address> addressFromCPUToMemory = new SynchronisedQueue<>(10);

        SynchronisedQueue<PCB> processesToBeRun = new SynchronisedQueue<>(10);

        SynchronisedQueue<Instruction> deadQueue1 = new SynchronisedQueue<>(10);
        SynchronisedQueue<String> deadQueue2 = new SynchronisedQueue<>(10);
        ArrayList<GraphData> deadQueue3 = new ArrayList<>(10);

        MemoryController memoryController = new MemoryController(deadQueue1, dataFromCPUToMemory,
                dataFromMemoryToCache, addressFromCacheToMemory, addressFromCPUToMemory, deadQueue2,
                deadQueue3, 5, 32);

        Cache cache = new Cache(dataFromCacheToCPU, dataFromMemoryToCache, addressFromCacheToMemory, addressFromCPUToCache, processesToBeRun, 32);

        PCB pcb = new PCB(0, new MemoryLimits(0, 4, 4), ProcessPriority.LOW);

        Instruction[] instructions = {
                new Instruction(Opcode.HDR, createOperand(4, null)),
                new Instruction(Opcode.ADD, createOperand(2, 3)),
                new Instruction(Opcode.SUB, createOperand(0, 45)),
                new Instruction(Opcode.MUL, createOperand(5, 10))
        };

        memoryController.start();

        for(int x = 0; x < instructions.length; x++){
            dataFromCPUToMemory.add(instructions[x]);
            addressFromCPUToMemory.add(new Address(0, x));
        }

        processesToBeRun.add(pcb);

        cache.start();

        delay();

        for(int x = 1; x < 4; x++){
            addressFromCPUToCache.add(new Address(0, x));
            assertEquals(instructions[x], dataFromCacheToCPU.remove());
        }
    }

    @Test
    public void cacheNotLoadedTest(){
        SynchronisedQueue<Instruction> dataFromCPUToMemory = new SynchronisedQueue<>(10);
        SynchronisedQueue<Instruction> dataFromMemoryToCache = new SynchronisedQueue<>(10);
        SynchronisedQueue<Instruction> dataFromCacheToCPU = new SynchronisedQueue<>(10);

        SynchronisedQueue<Address> addressFromCPUToCache =  new SynchronisedQueue<>(10);
        SynchronisedQueue<Address> addressFromCacheToMemory = new SynchronisedQueue<>(10);
        SynchronisedQueue<Address> addressFromCPUToMemory = new SynchronisedQueue<>(10);

        SynchronisedQueue<PCB> processesToBeRun = new SynchronisedQueue<>(10);

        SynchronisedQueue<Instruction> deadQueue1 = new SynchronisedQueue<>(10);
        SynchronisedQueue<String> deadQueue2 = new SynchronisedQueue<>(10);
        ArrayList<GraphData> deadQueue3 = new ArrayList<>(10);

        MemoryController memoryController = new MemoryController(deadQueue1, dataFromCPUToMemory,
                dataFromMemoryToCache, addressFromCacheToMemory, addressFromCPUToMemory, deadQueue2,
                deadQueue3, 5, 32);

        Cache cache = new Cache(dataFromCacheToCPU, dataFromMemoryToCache, addressFromCacheToMemory,
                addressFromCPUToCache, processesToBeRun, 32);

        Instruction[] instructions = {
                new Instruction(Opcode.HDR, createOperand(4, null)),
                new Instruction(Opcode.ADD, createOperand(2, 3)),
                new Instruction(Opcode.SUB, createOperand(0, 45)),
                new Instruction(Opcode.MUL, createOperand(5, 10))
        };

        memoryController.start();

        for(int x = 0; x < instructions.length; x++){
            dataFromCPUToMemory.add(instructions[x]);
            addressFromCPUToMemory.add(new Address(0, x));
        }

        cache.start();

        delay();

        for(int x = 1; x < 4; x++){
            addressFromCPUToCache.add(new Address(0, x));
            delay();
            assertEquals(instructions[x], dataFromCacheToCPU.remove());
        }
    }

    private Operand[] createOperand(int num1, Integer num2){
        if(num2 == null) return new Operand[]{new Operand(num1, AddressMode.IMMEDIATE)};
        return new Operand[]{new Operand(num1, AddressMode.IMMEDIATE), new Operand(num2, AddressMode.IMMEDIATE)};
    }

    private void delay(){
        try{
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
