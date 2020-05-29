import DataTypes.SynchronisedQueue;
import Memory.ram.MemoryController;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.MemoryAddress.Address;
import Shell.subsystemstats.GraphData;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MemoryTest {
    //MemoryController memory = new MemoryController(dataFromMemoryToCPU, dataFromCPUToMemory, dataFromMemoryToCache,
    // addressFromCacheToMemory, addressFromCPUToMemory, printQueue, graphData, amount, size);


    @Test
    public void allocationOfDataTest1(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.ADD, createOperand(4, 5))};
        MemoryController memory = initialiseCreationMemory(instructions);

        memory.start();
        delay();

        Instruction[] contents = memory.getMemoryContents()[0];

        for(int x = 0; x < 2; x++){
            assertEquals(instructions[x], contents[x]);
        }
    }

    @Test
    public void allocationOfDataTest2(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.MUL, createOperand(4, -60))};

        MemoryController memory = initialiseCreationMemory(instructions);

        memory.start();
        delay();

        Instruction[] contents = memory.getMemoryContents()[0];

        for(int x = 0; x < 2; x++){
            assertEquals(instructions[x], contents[x]);
        }
    }

    @Test
    public void retrievalOfDataTest1(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.ADD, createOperand(4, 5))};

        SynchronisedQueue<Instruction> data = new SynchronisedQueue<>(10);

        SynchronisedQueue<Address> address = initialiseRetrievalMemory(instructions, data, 0);

        delay();

        Instruction[] returnInstructions = new Instruction[2];

        address.add(new Address(0, 0));
        delay();
        returnInstructions[0] = data.remove();

        address.add(new Address(0, 1));
        delay();
        returnInstructions[1] = data.remove();

        for(int x = 0; x < 2; x++){
            assertEquals(instructions[x], returnInstructions[x]);
        }
    }

    @Test
    public void retrievalOfDataTest2(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.ADD, createOperand(4, 5)),
                new Instruction(Opcode.DAT, createOperand(1000, null))};

        SynchronisedQueue<Instruction> data = new SynchronisedQueue<>(10);

        SynchronisedQueue<Address> address = initialiseRetrievalMemory(instructions, data, 0);

        delay();

        address.add(new Address(0, 2));
        delay();
        assertEquals(instructions[2], data.remove());
    }

    @Test
    public void dataChangeTest1(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.ADD, createOperand(4, 5)),
                new Instruction(Opcode.DAT, createOperand(1000, null))};

        SynchronisedQueue<Address> address = new SynchronisedQueue<>(10);
        SynchronisedQueue<Instruction> data = new SynchronisedQueue<>(10);

        MemoryController memory = initialiseEditingMemory(instructions, address, data);

        memory.start();

        Instruction beforeEditing = memory.getMemoryContents()[0][2];

        data.add(new Instruction(Opcode.DAT, createOperand(1, null)));
        address.add(new Address(0, 2));

        delay();

        Instruction afterEditing = memory.getMemoryContents()[0][2];

        assertNotEquals(afterEditing, beforeEditing);

        assertEquals(new Instruction(Opcode.DAT, createOperand(1, null)), afterEditing);
    }

    @Test
    public void dataDeletionTest1(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.ADD, createOperand(4, 5)),
                new Instruction(Opcode.DAT, createOperand(1000, null))};

        SynchronisedQueue<Instruction> data =  new SynchronisedQueue<>(10);

        SynchronisedQueue<Address> address = initialiseRetrievalMemory(instructions, data, 0);

        address.add(new Address(0, -1));

        delay();

        address.add(new Address(0, 0));

        delay();

        assertEquals(new Instruction(Opcode.ERR,
                new Operand[]{
                        new Operand("java.lang.RuntimeException: " +
                                "Memory address requested is out of bounds of program space",
                                AddressMode.IMMEDIATE)}),
                data.remove());
    }

    @Test
    public void dataDeletionTest2(){
        Instruction[] instructions = new Instruction[]{new Instruction(Opcode.HDR, createOperand(2, null)),
                new Instruction(Opcode.ADD, createOperand(4, 5)),
                new Instruction(Opcode.DAT, createOperand(1000, null))};

        SynchronisedQueue<Instruction> data =  new SynchronisedQueue<>(10);

        SynchronisedQueue<Address> address = initialiseRetrievalMemory(instructions, data, 5);

        address.add(new Address(5, -1));

        delay();

        address.add(new Address(5, 0));

        delay();

        assertEquals(new Instruction(Opcode.ERR,
                        new Operand[]{
                                new Operand("java.lang.RuntimeException: " +
                                        "Memory address requested is out of bounds of program space",
                                        AddressMode.IMMEDIATE)}),
                data.remove());
    }

    private MemoryController initialiseEditingMemory(Instruction[] instructions,
                                                     SynchronisedQueue<Address> addressQueue,
                                                     SynchronisedQueue<Instruction> data){
        SynchronisedQueue<Instruction> deadQueue = new SynchronisedQueue<>(1);
        SynchronisedQueue<Address> deadQueue2 = new SynchronisedQueue<>(1);
        SynchronisedQueue<String> deadQueue3 = new SynchronisedQueue<>(1);
        ArrayList<GraphData> deadQueue4 = new ArrayList<>();

        int address = 0;

        for(Instruction instruction : instructions){
            data.add(instruction);
            addressQueue.add(new Address(0, address++));
        }

        return new MemoryController(deadQueue, data, deadQueue,
                deadQueue2, addressQueue, deadQueue3, deadQueue4, 5, 32);
    }

    private MemoryController initialiseCreationMemory(Instruction[] instructions){
        SynchronisedQueue<Instruction> deadQueue = new SynchronisedQueue<>(1);
        SynchronisedQueue<Address> deadQueue2 = new SynchronisedQueue<>(1);
        SynchronisedQueue<String> deadQueue3 = new SynchronisedQueue<>(1);
        ArrayList<GraphData> deadQueue4 = new ArrayList<>();

        SynchronisedQueue<Instruction> dataQueue = new SynchronisedQueue<>(instructions.length);
        SynchronisedQueue<Address> addressQueue = new SynchronisedQueue<>(instructions.length);

        int address = 0;

        for(Instruction instruction : instructions){
            dataQueue.add(instruction);
            addressQueue.add(new Address(0, address++));
        }

        return new MemoryController(deadQueue, dataQueue, deadQueue,
                deadQueue2, addressQueue, deadQueue3, deadQueue4, 5, 32);
    }

    private SynchronisedQueue<Address> initialiseRetrievalMemory(Instruction[] instructions,
                                                       SynchronisedQueue<Instruction> returnQueue, int pid){
        SynchronisedQueue<Instruction> deadQueue = new SynchronisedQueue<>(1);
        SynchronisedQueue<Address> deadQueue2 = new SynchronisedQueue<>(1);
        SynchronisedQueue<String> deadQueue3 = new SynchronisedQueue<>(100);
        ArrayList<GraphData> deadQueue4 = new ArrayList<>();

        SynchronisedQueue<Instruction> dataQueue = new SynchronisedQueue<>(instructions.length);
        SynchronisedQueue<Address> addressQueue = new SynchronisedQueue<>(instructions.length);

        int address = 0;

        for(Instruction instruction : instructions){
            dataQueue.add(instruction);
            addressQueue.add(new Address(pid, address++));
        }

        new MemoryController(returnQueue, dataQueue, deadQueue,
                deadQueue2, addressQueue, deadQueue3, deadQueue4, 5, 32).start();

        return addressQueue;
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
