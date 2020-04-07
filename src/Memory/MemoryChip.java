package Memory;

import ProcessFormats.Opcode.Opcode;

public class MemoryChip {
    private Opcode[][] memoryArray;

    public MemoryChip(int memorySize){
        this.memoryArray = createMemory(memorySize);
    }

    private Opcode[][] createMemory(int size){
        int squareSize = (int)Math.sqrt(size);
        if(squareSize * squareSize != size){
            squareSize = squareSize+1;
        }
        System.out.println("Memory size could not be evenly split, memory size has been changed to:" + (squareSize*squareSize));
        return new Opcode[squareSize][squareSize];
    }

    public void setData(int x, int y, Opcode data){
        memoryArray[x][y] = data;
    }

    public Opcode getData(int x, int y){
        return memoryArray[x][y];
    }
}
