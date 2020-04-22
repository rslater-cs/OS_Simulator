package Memory;

import DataTypes.BiDirectionalQueue;
import DataTypes.SynchronisedQueue;
import Memory.Pointers.MemoryDataPointer;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Opcode.ArgumentObjects.AddressMode;
import ProcessFormats.Data.Opcode.ArgumentObjects.Argument;
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
        this.openDataPoints.add(new MemoryDataPointer(0, memoryChipSize*memoryChipSize));
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
                int absoluteAddress;
                if(dataQueue.senderSize() > 0){
                    Opcode opcode = dataQueue.receive();
                    if(opcode.getProcess().equals("header")){
                        absoluteAddress = createProgramSpace(relativeAddress.getPid(), opcode.getIntArg(0));
                    }else{
                        absoluteAddress = getAbsoluteAddress(relativeAddress);
                    }
                    if(absoluteAddress == -1){
                        printError("Memory failed to store data, due to low available storage or missing process identification");
                    }else{
                        final int[] addresses = decodeAddress(absoluteAddress);
                        memoryChip.setData(addresses[0], addresses[1], opcode);
                    }
                    System.out.println("address: " + absoluteAddress + ", opcode: " + opcode);
                } else{
                    absoluteAddress = getAbsoluteAddress(relativeAddress);
                    final int[] addresses = decodeAddress(absoluteAddress);
                    dataQueue.reply(memoryChip.getData(addresses[0], addresses[1]));
                }
            }
        }
    }

    public int[] decodeAddress(int address){
        return new int[]{address%memoryChipSize, address/memoryChipSize};
    }

    private int getAbsoluteAddress(Address relativeAddress){
        if(absoluteAddresses.containsKey(relativeAddress.getPid())){
            final int absoluteAddress = relativeAddress.getAddress() + absoluteAddresses.get(relativeAddress.getPid()).getStart();
            if(absoluteAddress >= absoluteAddresses.get(relativeAddress.getPid()).getEnd()) return -1;
            return absoluteAddress;
        }
        return -1;
    }

    private int createProgramSpace(int pid, int spaceSize){
        int spaceDifference = Integer.MAX_VALUE;
        MemoryDataPointer optimalSpace = null;
        for(MemoryDataPointer pointer : openDataPoints){
            final int difference = pointer.getBounds() - spaceSize;
            if(difference < spaceDifference && difference >= 0){
                spaceDifference = difference;
                optimalSpace = pointer;
            }
        }
        if(optimalSpace != null){
            MemoryDataPointer perfectSpace = splitPointer(optimalSpace, spaceSize);
            absoluteAddresses.put(pid, perfectSpace);
            return perfectSpace.getStart();
        }
        return -1;
    }

    private MemoryDataPointer splitPointer(MemoryDataPointer pointer, int size){
        final int end = pointer.getStart()+size;
        MemoryDataPointer finalPointer = new MemoryDataPointer(pointer.getStart(), end);
        pointer.setStart(end);
        return finalPointer;
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

    private void printError(String errorMessage){
        dataQueue.reply(new Opcode("err", new Argument[]{new Argument(new RuntimeException(errorMessage).toString(), AddressMode.NONE)}));
    }

    @Override
    public String toString() {
        StringBuffer memorySummary = new StringBuffer();

        for(int y = 0; y < memoryChipSize; y++){
            for(int x = 0; x < memoryChipSize; x++){
                memorySummary.append("address: ");
                memorySummary.append(x);
                memorySummary.append(y);
                memorySummary.append(", data: ");
                memorySummary.append(memoryChip.getData(x, y));
                memorySummary.append("\n");
            }
        }

        return memorySummary.toString();
    }
}
