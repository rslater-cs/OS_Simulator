package ProcessFormats.ProcessControlBlock.InternalObjects;

public class ProcessTime {
    private long start = 0;
    private long end = 0;

    public void setStart(){
        this.start = System.currentTimeMillis();
    }

    public void setEnd(){
        this.end = System.currentTimeMillis();
    }

    public double totalTime(){
        return (end - start) / 1000.0;
    }
}
