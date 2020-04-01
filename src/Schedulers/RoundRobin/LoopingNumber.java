package Schedulers.RoundRobin;

public class LoopingNumber {
    private int maxNum;
    private int currentNum;

    public LoopingNumber(int maxNum){
        this.maxNum = maxNum;
    }

    public boolean hasLooped(){
        return(inc() == 0);
    }

    public int inc(){
        return currentNum++ % maxNum;
    }
}
