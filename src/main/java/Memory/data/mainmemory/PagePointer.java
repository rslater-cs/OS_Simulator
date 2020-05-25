package Memory.data.mainmemory;

public class PagePointer {
    private int start;
    private int end;
    private int[] frames;

    public PagePointer(int start, int end){
        this.start = start;
        this.end = end;
        this.frames = makeFrames();
    }

    public int getStart(){
        return start;
    }

    public void setStart(int start){
        this.start = start;
        this.frames = makeFrames();
    }

    public int getEnd(){
        return end;
    }

    public void setEnd(int end){
        this.end = end;
        this.frames = makeFrames();
    }

    public int getBounds(){
        return end - start;
    }

    public void setFrame(int index, int frame){
        if(index < getBounds()) frames[index] = frame;
    }

    public int getFrame(int index){
        if(index < getBounds()) return frames[index];
        throw new IndexOutOfBoundsException();
    }

    public int[] makeFrames(){
        int[] frames = new int[getBounds()];
        for(int x = 0; x < getBounds(); x++){
            frames[x] = x + start;
        }
        return frames;
    }
}
