package Memory;

import java.util.ArrayList;

public class MemoryDataPointer {
    private int start;
    private int end;

    public MemoryDataPointer(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart(){
        return start;
    }

    public void setStart(int start){
        this.start = start;
    }

    public int getEnd(){
        return end;
    }

    public void setEnd(int end){
        this.end = end;
    }

    public int getBounds(){
        return end - start;
    }

    public int dec(){
        return start++;
    }

}
