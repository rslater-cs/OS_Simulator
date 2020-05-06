package Memory.ram;

import ProcessFormats.Data.Instruction.Instruction;

public class MemoryChip {
    private Instruction[][] memoryArray;

    public MemoryChip(int memorySize){
        this.memoryArray = new Instruction[memorySize][memorySize];
    }

    public void setData(int x, int y, Instruction data){
        memoryArray[x][y] = data;
    }

    public Instruction getData(int x, int y){
        return memoryArray[x][y];
    }
}
