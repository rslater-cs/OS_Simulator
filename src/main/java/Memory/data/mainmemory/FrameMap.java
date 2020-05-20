package Memory.data.mainmemory;

import java.util.HashMap;
import java.util.Map;

public class FrameMap {
    private Map<Integer, Integer> framesToPage = new HashMap<>();

    public void add(int frame, int page){
        if(frame >= 0 && page >= 0){
            framesToPage.put(frame, page);
        }
    }

    public int getPage(int frame){
        if(framesToPage.containsKey(frame)){
            return framesToPage.get(frame);
        }
        return -1;
    }

    public void deletePage(int frame){
        if(framesToPage.containsKey(frame)){
            framesToPage.remove(frame);
        }
    }
}
