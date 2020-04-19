package Memory;

import ProcessFormats.Data.Opcode.Opcode;

public class MemoryChip {
    private Opcode[][] memoryArray;

    public MemoryChip(int memorySize){
        this.memoryArray = new Opcode[memorySize][memorySize];
    }

    public void setData(int x, int y, Opcode data){
        memoryArray[x][y] = data;
    }

    public Opcode getData(int x, int y){
        return memoryArray[x][y];
    }
}
