package Memory;

import DataTypes.BiDirectionalQueue;
import DataTypes.SynchronisedQueue;
import Memory.Pointers.MemoryDataPointer;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Opcode.Opcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryController extends Thread{
    private MemoryChip memoryChip;
    private int memoryChipSize;
    private Map<Integer, MemoryDataPointer> absoluteAddresses = new HashMap<>();
    private ArrayList<MemoryDataPointer> openDataPoints = new ArrayList<>();
    private BiDirectionalQueue<Opcode> dataQueue;
    private SynchronisedQueue<Address> addressQueue;
    private boolean computerIsRunning;

    public MemoryController(BiDirectionalQueue<Opcode> dataQueue, SynchronisedQueue<Address> addressQueue, int memorySize, boolean computerIsRunning){
        this.memoryChipSize = fixSize(memorySize);
        this.memoryChip = new MemoryChip(memoryChipSize);
        this.dataQueue = dataQueue;
        this.addressQueue = addressQueue;
        this.computerIsRunning = computerIsRunning;
        this.openDataPoints.add(new MemoryDataPointer(0, memoryChipSize));
    }

    private int fixSize(int size){
        int squareSize = (int)Math.sqrt(size);
        if(squareSize * squareSize != size){
            squareSize = squareSize+1;
            System.out.println("Memory size could not be evenly split, memory size has been changed to: " + (squareSize*squareSize));
        }
        return squareSize;
    }

    public void run(){
        while(computerIsRunning){
            final Address relativeAddress = addressQueue.remove();
            if(relativeAddress.getAddress() == -1){
                deleteData(relativeAddress.getPid());
            } else{
                final int absoluteAddress = getAbsoluteAddress(relativeAddress);
                final int xAddress = absoluteAddress / memoryChipSize;
                final int yAddress = absoluteAddress % memoryChipSize;
                if(dataQueue.senderSize() > 0){
                    Opcode opcode = dataQueue.receive();
                    memoryChip.setData(xAddress, yAddress, opcode);
                } else{
                    dataQueue.reply(memoryChip.getData(xAddress, yAddress));
                }
            }
        }
    }

    private int getAbsoluteAddress(Address relativeAddress){
        if(absoluteAddresses.containsKey(relativeAddress)){
            return relativeAddress.getAddress() + absoluteAddresses.get(relativeAddress.getPid()).getStart();
        }
        return relativeAddress.getAddress() + findStoragePoint(relativeAddress.getPid());
    }

    private int findStoragePoint(int pid){
        MemoryDataPointer freeSpace = largestFreeSpace();
        if(freeSpace == null) throw new IndexOutOfBoundsException("Memory full, data could not be stored");
        return freeSpace.dec();
    }

    private MemoryDataPointer largestFreeSpace(){
        MemoryDataPointer freeSpace = null;
        int maxSpace = 0;
        for(MemoryDataPointer pointer : openDataPoints){
            if(pointer.getBounds() == 0){
                openDataPoints.remove(pointer);
            }else if(pointer.getBounds() > maxSpace){
                maxSpace = pointer.getBounds();
                freeSpace = pointer;
            }
        }
        return freeSpace;
    }

    private void deleteData(int pid){
        if(!joinPointers(absoluteAddresses.get(pid))) openDataPoints.add(absoluteAddresses.get(pid));
        absoluteAddresses.remove(pid);
    }

    private boolean joinPointers(MemoryDataPointer pointer){
        boolean hasJoined = false;
        for(int x = 0; x < openDataPoints.size(); x++){
            if(openDataPoints.get(x).getStart() == pointer.getEnd()){
                openDataPoints.get(x).setStart(pointer.getStart());
                hasJoined = true;
            } else if(openDataPoints.get(x).getEnd() == pointer.getStart()){
                openDataPoints.get(x).setEnd(pointer.getEnd());
                hasJoined = true;
            }
        }
        return hasJoined;
    }
}
