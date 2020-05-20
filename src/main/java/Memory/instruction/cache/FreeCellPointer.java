package Memory.instruction.cache;

public class FreeCellPointer {
    private int[] freePoints;

    public FreeCellPointer(int length){
        freePoints = new int[length];
        for(int x = 0; x < length; x++){
            freePoints[x] = x;
        }
    }

    public void addFreePoint(int index){
        freePoints[index] = index;
    }

    public void deleteFreePoint(int index){
        freePoints[index] = -1;
    }

    public boolean isFreeSpace(){
        for(int x = 0; x < freePoints.length; x++){
            if(freePoints[x] == x) return true;
        }
        return false;
    }

    public int nextFreeSpace(){
        for(int x = 0; x < freePoints.length; x++){
            if(freePoints[x] == x){
                freePoints[x] = -1;
                return x;
            }
        }
        return -1;
    }
}
