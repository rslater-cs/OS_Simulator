package Memory.data;

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

    @Override
    public String toString() {
        StringBuilder memory = new StringBuilder();

        for(int x = 0; x < memoryArray.length; x++){
            memory.append("Page Number ");
            memory.append(x);
            memory.append("\n");
            for(int y = 0; y < memoryArray[0].length; y++){
                memory.append(memoryArray[x][y]);
                memory.append("\n");
            }
        }

        return memory.toString();
    }
}
