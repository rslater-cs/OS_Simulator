package Memory;

import DataTypes.BiDirectionalQueue;
import DataTypes.SynchronisedQueue;

import java.util.HashMap;
import java.util.Map;

public class MemoryController {
    private MemoryChip memoryChip;
    private int memoryPointer = 0;
    private Map<Integer, Integer> absoluteAddresses = new HashMap<>();
    private BiDirectionalQueue<Data> dataQueue;
    private SynchronisedQueue<Integer> addressQueue;

    public MemoryController(BiDirectionalQueue<Data> dataQueue, SynchronisedQueue<Integer> addressQueue, int memorySize){
        this.memoryChip = new MemoryChip(memorySize);
        this.dataQueue = dataQueue;
        this.addressQueue = addressQueue;
    }
}
