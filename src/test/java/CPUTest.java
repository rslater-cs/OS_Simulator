import datatypes.SynchronisedQueue;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.ProcessControlBlock.InternalObjects.MemoryLimits;
import ProcessFormats.ProcessControlBlock.InternalObjects.ProcessPriority;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CPUTest {

    @Test
    public void addTest(){
        CPU cpu = new CPU(null, null, null,
                null, null, null,
                null, null, 1);

        cpu.execute(new Instruction(Opcode.ADD, new Operand[]{new Operand(5, AddressMode.IMMEDIATE), new Operand(20, AddressMode.IMMEDIATE)}));

        assertEquals(25, cpu.getReturnRegister().getIntArgument());
    }

    @Test
    public void subTest(){
        CPU cpu = new CPU(null, null, null,
                null, null, null,
                null, null, 1);
        cpu.execute(new Instruction(Opcode.SUB, new Operand[]{new Operand(20, AddressMode.IMMEDIATE), new Operand(5, AddressMode.IMMEDIATE)}));

        assertEquals(15, cpu.getReturnRegister().getIntArgument());
    }

    @Test
    public void mulTest(){
        CPU cpu = new CPU(null, null, null,
                null, null, null,
                null, null, 1);
        cpu.execute(new Instruction(Opcode.MUL, new Operand[]{new Operand(20, AddressMode.IMMEDIATE), new Operand(5, AddressMode.IMMEDIATE)}));

        assertEquals(100, cpu.getReturnRegister().getIntArgument());
    }

    @Test
    public void divTest(){
        CPU cpu = new CPU(null, null, null,
                null, null, null,
                null, null, 1);
        cpu.execute(new Instruction(Opcode.DIV, new Operand[]{new Operand(20, AddressMode.IMMEDIATE), new Operand(5, AddressMode.IMMEDIATE)}));

        assertEquals(4, cpu.getReturnRegister().getIntArgument());
    }

    @Test
    public void errTest(){
        PCB pcb = new PCB(5,new MemoryLimits(0,4,4), ProcessPriority.HIGH);
        SynchronisedQueue<String> printQueue = new SynchronisedQueue<>(1);
        CPU cpu = new CPU(null, null, null,
                null, null, null,
                null, printQueue, 1);
        cpu.testSetPCB(pcb);
        cpu.execute(new Instruction(Opcode.ERR, new Operand[]{new Operand(20, AddressMode.IMMEDIATE), new Operand(5, AddressMode.IMMEDIATE)}));

        assertEquals("[Process 5 output] cpu err at process: 'ERR'", printQueue.remove());
    }

    @Test
    public void outTest(){
        PCB pcb = new PCB(5,new MemoryLimits(0,4,4), ProcessPriority.HIGH);
        SynchronisedQueue<String> printQueue = new SynchronisedQueue<>(10);
        CPU cpu = new CPU(null, null, null,
                null, null, null,
                null, printQueue, 1);
        cpu.testSetPCB(pcb);
        cpu.execute(new Instruction(Opcode.OUT, new Operand[]{new Operand(2000, AddressMode.IMMEDIATE)}));

        assertEquals("[Process 5 output] 2000", printQueue.remove());
    }
}
