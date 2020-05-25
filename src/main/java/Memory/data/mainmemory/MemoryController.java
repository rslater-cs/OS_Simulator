package Memory.data.mainmemory;


import DataTypes.SynchronisedQueue;
import Memory.data.MemoryChip;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.Instruction.Instruction;
import Shell.subsystemstats.GraphData;

import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryController extends Thread{
    private MemoryChip memoryChip;

    private int pageSize;
    private int amountOfPages;

    private FrameMap frames;

    private SynchronisedQueue<Instruction> dataFromMemoryToCPU;
    private SynchronisedQueue<Instruction> dataFromCPUToMemory;
    private SynchronisedQueue<Instruction> dataFromMemoryToCache;

    private SynchronisedQueue<Address> addressFromCacheToMemory;
    private SynchronisedQueue<Address> addressFromCPUToMemory;

    private SynchronisedQueue<String> printQueue;

    private ArrayList<GraphData> graphData;

    private boolean computerIsRunning = true;

    public MemoryController(SynchronisedQueue<Instruction> dataFromMemoryToCPU,
                            SynchronisedQueue<Instruction> dataFromCPUToMemory,
                            SynchronisedQueue<Instruction> dataFromMemoryToCache,
                            SynchronisedQueue<Address> addressFromCacheToMemory,
                            SynchronisedQueue<Address> addressFromCPUToMemory,
                            SynchronisedQueue<String> printQueue,
                            ArrayList<GraphData> graphData,
                            int amountOfPages, int pageSize){
        this.amountOfPages = amountOfPages;
        this.pageSize = pageSize;
        this.memoryChip = new MemoryChip(amountOfPages, pageSize);

        this.dataFromMemoryToCPU = dataFromMemoryToCPU;
        this.dataFromCPUToMemory = dataFromCPUToMemory;
        this.dataFromMemoryToCache = dataFromMemoryToCache;

        this.addressFromCPUToMemory = addressFromCPUToMemory;
        this.addressFromCacheToMemory = addressFromCacheToMemory;

        this.printQueue = printQueue;

        this.graphData = graphData;

        this.frames = new FrameMap(amountOfPages);
    }

    public void run(){
        while(computerIsRunning){
            Address relativeAddress = null;
            SynchronisedQueue<Instruction> returnQueue = null;
            if(addressFromCacheToMemory.size() > 0){
                relativeAddress = addressFromCacheToMemory.remove();
                returnQueue = dataFromMemoryToCache;
            }else if(addressFromCPUToMemory.size() > 0){
                relativeAddress = addressFromCPUToMemory.remove();
                returnQueue = dataFromMemoryToCPU;
            }

            if(relativeAddress != null && returnQueue != null) {
                if (relativeAddress.getAddress() == -1) {
                    deleteData(relativeAddress.getPid());
                    graphData.add(new GraphData(getPercentUsage(), System.currentTimeMillis()/1000));
                } else {
                    int[] absoluteAddress;
                    if (dataFromCPUToMemory.size() > 0 && returnQueue == dataFromMemoryToCPU) {
                        Instruction instruction = dataFromCPUToMemory.remove();

                        if (instruction.getProcess() == Opcode.HDR) {
                            absoluteAddress = createProgramSpace(relativeAddress.getPid(), instruction.getArg(0).getIntArgument());
                            graphData.add(new GraphData(getPercentUsage(), System.currentTimeMillis()/1000));
                        } else {
                            absoluteAddress = getAbsoluteAddress(relativeAddress);
                        }
                        if (absoluteAddress == null) {
                            printError("Memory failed to store data, due to low available storage or missing process identification");
                        } else {
                            Instruction[] page = memoryChip.getData(absoluteAddress[0]);
                            page[absoluteAddress[1]] = instruction;
                            memoryChip.setData(absoluteAddress[0], page);
                        }
                    } else {
                        absoluteAddress = getAbsoluteAddress(relativeAddress);
                        if (absoluteAddress == null) {
                            printError("Memory address requested is out of bounds of program space");
                            returnQueue.add(new Instruction(Opcode.ERR, null));
                        } else {
                            Instruction[] page = memoryChip.getData(absoluteAddress[0]);
                            returnQueue.add(page[absoluteAddress[1]]);
                        }
                    }
                }
            }
        }
    }

    public int decodeAddress(int address){
        return address / pageSize;
    }

    private int[] getAbsoluteAddress(Address relativeAddress){
        if(frames.pidExists(relativeAddress.getPid())){
            int frameNeeded = decodeAddress(relativeAddress.getAddress());
            PagePointer pointer = frames.getFramePointer(relativeAddress.getPid());
            int frame = pointer.getFrame(frameNeeded);
            return new int[]{frame, relativeAddress.getAddress()%pageSize};
        }
        return null;
    }

    private PagePointer createProgramSpace(int pid, int spaceSize) {
        boolean pageFound = true;
        int spaceNeeded = decodeAddress(spaceSize);
        int minDifference = Integer.MAX_VALUE;
        if(spaceSize % pageSize > 0) spaceNeeded++;
        int optimalSpace = -1;
        int maxHole = -1;
        int maxHoleIndex = -1;
        final ArrayList<PagePointer> freePoints = frames.getFreePoints();

        for(int x = 0; x < freePoints.size(); x++){
            final int difference = freePoints.get(x).getBounds() - spaceNeeded;
            if(freePoints.get(x).getBounds() > maxHole){
                maxHoleIndex = x;
                maxHole = freePoints.get(maxHoleIndex).getBounds();
            }
            if(difference < minDifference && difference >= 0){
                minDifference = difference;
                optimalSpace = x;
            }
        }
        PagePointer perfectSpace;
        int size = spaceNeeded;
        if(optimalSpace == -1) {
            optimalSpace = maxHoleIndex;
            pageFound = false;
            size = freePoints.get(optimalSpace).getBounds();
        }
        perfectSpace = splitPointer(freePoints.get(optimalSpace), size);
        if(!pageFound){
            perfectSpace = frameReplace(spaceNeeded, pid, perfectSpace);
        }
        addPointer(perfectSpace, pid);
        return perfectSpace;
        /*int pagesNeeded = decodeAddress(spaceSize)+1;
        int spaceDifference = Integer.MAX_VALUE;
        PagePointer optimalSpace = null;
        for (PagePointer pointer : openDataPoints) {
            final int difference = pointer.getBounds() - pagesNeeded;
            if (difference < spaceDifference && difference >= 0) {
                spaceDifference = difference;
                optimalSpace = pointer;
            }
        }
        if (optimalSpace != null) {
            PagePointer perfectSpace = splitPointer(optimalSpace, pagesNeeded);
            print("Creating data pointer " + pointerSummary(perfectSpace));
            absoluteAddresses.put(pid, perfectSpace);
            return new int[]{perfectSpace.getStart(), 0};
        }
        return new int[]{-1};*/
    }

    private void addPointer(PagePointer pointer, int pid){
        print("Creating data pointer " + pointerSummary(pointer));
        frames.add(pid, pointer);
    }

    private PagePointer frameReplace(int spaceNeeded, int pid, PagePointer currentSpace){
        spaceNeeded -= currentSpace.getBounds();
        if(frames.pidExists(pid-1)){
            for(int x = 0; x < spaceNeeded; x++){

            }
        }
    }

    private PagePointer splitPointer(PagePointer pointer, int size){
        final int end = pointer.getStart()+size;
        PagePointer finalPointer = new PagePointer(pointer.getStart(), end);
        pointer.setStart(end);
        return finalPointer;
    }

    private void deleteData(int pid){
        PagePointer pointer = absoluteAddresses.get(pid);
        openDataPoints.add(pointer);
        print("Deleting pointer " + pointerSummary(pointer));
        absoluteAddresses.remove(pid);
    }

    private boolean joinPointers(PagePointer pointer){
        boolean hasJoined = false;
        for(int x = 0; x < openDataPoints.size(); x++){
            if(openDataPoints.get(x).getStart() == pointer.getEnd()){
                print("Joining pointers " + pointerSummary(openDataPoints.get(x)) + " and " + pointerSummary(pointer));
                openDataPoints.get(x).setStart(pointer.getStart());
                hasJoined = true;
            } else if(openDataPoints.get(x).getEnd() == pointer.getStart()){
                print("Joining pointers " + pointerSummary(openDataPoints.get(x)) + " and " + pointerSummary(pointer));
                openDataPoints.get(x).setEnd(pointer.getEnd());
                hasJoined = true;
            }
        }
        return hasJoined;
    }

    private String pointerSummary(PagePointer pointer){
        return pointer.getStart() + " to " + pointer.getEnd();
    }

    private void print(String message){
        printQueue.add("[Memory Status] " + message);
    }

    private void printError(String errorMessage){
        dataFromMemoryToCPU.add(new Instruction(Opcode.ERR, new Operand[]{new Operand(new RuntimeException(errorMessage).toString(), AddressMode.NONE)}));
    }

    private double getPercentUsage(){
        int openPoints = 0;
        for(PagePointer pointer: openDataPoints){
            System.out.println(pointer.getStart() + " " + pointer.getBounds() + " " + pointer.getEnd());
            openPoints += pointer.getBounds();
        }
        System.out.println(openPoints);
        System.out.println(amountOfPages);
        return (double)openPoints / (double)amountOfPages;
    }

    public void endThread(){
        computerIsRunning = false;
    }

    @Override
    public String toString() {
        StringBuilder memorySummary = new StringBuilder();

        memorySummary.append("page size: ");
        memorySummary.append(pageSize*amountOfPages);

        for(int y = 0; y < amountOfPages; y++){
            for(Instruction instruction : memoryChip.getData(y)){
                memorySummary.append("\n");
                memorySummary.append(instruction);
            }
        }

        return memorySummary.toString();
    }
}
