package Memory.ram;

import ProcessFormats.Data.Instruction.Instruction;

public class MemoryChip {
    private Instruction[][] memoryArray;

    public MemoryChip(int amountOfPages, int pageSize){
        this.memoryArray = new Instruction[amountOfPages][pageSize];
    }

    public void setData(int page, Instruction[] data){
        memoryArray[page] = data;
    }

    public Instruction[] getData(int page){
        return memoryArray[page];
    }
}
