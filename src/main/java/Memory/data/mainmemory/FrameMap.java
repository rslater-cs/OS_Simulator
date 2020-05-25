package Memory.data.mainmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FrameMap {
    private Map<Integer, PagePointer> map = new HashMap<>();
    private ArrayList<PagePointer> openPoints = new ArrayList<>();
    private int[] lastUsedPrograms = new int[2];

    public FrameMap(int pageAmount){
        openPoints.add(new PagePointer(0, pageAmount));
    }

    public void add(int pid, PagePointer frames){
        if(pid >= 0 && frames != null && !map.containsKey(pid)) map.put(pid, frames);
    }

    public void delete(int pid){
        if(pid >= 0 && map.containsKey(pid)) map.remove(pid);
    }

    public void change(int pid, PagePointer frames){
        delete(pid);
        add(pid, frames);
    }

    public ArrayList<PagePointer> getFreePoints(){
        return openPoints;
    }

    public boolean pidExists(int pid){
        return map.containsKey(pid);
    }

    public PagePointer getFramePointer(int pid){
        if(pid >= 0 && map.containsKey(pid)){
            if(pid != lastUsedPrograms[0]){
                lastUsedPrograms[1] = lastUsedPrograms[0];
                lastUsedPrograms[0] = pid;
            }
            return map.get(pid);
        }
        return null;
    }

    public int getLastExecutedProgram(){
        return lastUsedPrograms[1];
    }
    
}
