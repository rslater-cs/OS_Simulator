package Memory.data.mainmemory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrameMap {
    private Map<Integer, Integer> IDToPage = new HashMap<>();

    public FrameMap(int size){
        for(int x = 0; x < size; x++){
            IDToPage.put(-1, 0);
        }
    }

    public void add(int frame, int page){
        if(frame >= 0 && page >= 0){
            IDToPage.put(frame, page);
        }
    }

    public int getPage(int frame){
        if(IDToPage.containsKey(frame)){
            return IDToPage.get(frame);
        }
        return -1;
    }

    public void deletePage(int frame){
        if(IDToPage.containsKey(frame)){
            IDToPage.remove(frame);
            IDToPage.put(-1, 0);
        }
    }

    public int[] reserveSpace(int size, int pid){
        int start = 0;
        int end = 0;
        int length = 0;
    }
}
