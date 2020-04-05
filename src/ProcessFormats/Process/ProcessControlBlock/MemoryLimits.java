package ProcessFormats.Process.ProcessControlBlock;

public class MemoryLimits {
    private int start;
    private int end;
    private int size;

    public MemoryLimits(int start, int end, int size){
        this.start = start;
        this.end = end;
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getSize() {
        return size;
    }
}
