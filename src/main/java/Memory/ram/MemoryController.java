package Memory.ram;


import datatypes.SynchronisedQueue;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.Instruction.Instruction;
import Shell.subsystemstats.GraphData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryController extends Thread{
    private MemoryChip memoryChip;

    private int pageSize;
    private int amountOfPages;

    private SynchronisedQueue<Instruction> dataFromMemoryToCPU;
    private SynchronisedQueue<Instruction> dataFromCPUToMemory;
    private SynchronisedQueue<Instruction> dataFromMemoryToCache;

    private SynchronisedQueue<Address> addressFromCacheToMemory;
    private SynchronisedQueue<Address> addressFromCPUToMemory;

    private SynchronisedQueue<String> printQueue;

    private ArrayList<GraphData> graphData;

    private boolean computerIsRunning = true;

    private ArrayList<FramePointer> openDataPoints = new ArrayList<>();

    private Map<Integer, FramePointer> absoluteAddresses = new HashMap<>();

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

        this.openDataPoints.add(new FramePointer(0, amountOfPages));
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
                    int[] absoluteAddress = getAbsoluteAddress(relativeAddress);
                    if (dataFromCPUToMemory.size() > 0 && returnQueue == dataFromMemoryToCPU) {
                        Instruction instruction = dataFromCPUToMemory.remove();

                        if (instruction.getProcess() == Opcode.HDR) {
                            absoluteAddress = createProgramSpace(relativeAddress.getPid(), instruction.getArg(0).getIntArgument());
                            graphData.add(new GraphData(getPercentUsage(), System.currentTimeMillis()/1000));
                        }
                        if (absoluteAddress[0] == -1) {
                            printError("Memory failed to store data, due to low available storage or missing process identification");
                        } else {
                            Instruction[] page = memoryChip.getData(absoluteAddress[0]);
                            page[absoluteAddress[1]] = instruction;
                            memoryChip.setData(absoluteAddress[0], page);
                        }
                    } else {
                        if (absoluteAddress[0] == -1) {
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
        if(absoluteAddresses.containsKey(relativeAddress.getPid())){
            int pageNeeded = decodeAddress(relativeAddress.getAddress());
            pageNeeded += absoluteAddresses.get(relativeAddress.getPid()).getStart();
            return new int[]{pageNeeded, relativeAddress.getAddress()%pageSize};
        }
        return new int[]{-1};
    }

    private int[] createProgramSpace(int pid, int spaceSize) {
        int pagesNeeded = decodeAddress(spaceSize)+1;
        int spaceDifference = Integer.MAX_VALUE;
        FramePointer optimalSpace = null;
        for (FramePointer pointer : openDataPoints) {
            final int difference = pointer.getBounds() - pagesNeeded;
            if (difference < spaceDifference && difference >= 0) {
                spaceDifference = difference;
                optimalSpace = pointer;
            }
        }
        if (optimalSpace != null) {
            FramePointer perfectSpace = splitPointer(optimalSpace, pagesNeeded);
            print("Creating data pointer " + pointerSummary(perfectSpace));
            absoluteAddresses.put(pid, perfectSpace);
            return new int[]{perfectSpace.getStart(), 0};
        }
        return new int[]{-1};
    }

    private FramePointer splitPointer(FramePointer pointer, int size){
        final int end = pointer.getStart()+size;
        FramePointer finalPointer = new FramePointer(pointer.getStart(), end);
        pointer.setStart(end);
        return finalPointer;
    }

    private void deleteData(int pid){
        FramePointer pointer = absoluteAddresses.get(pid);
        if(!joinPointers(pointer)) openDataPoints.add(pointer);
        print("Deleting pointer " + pointerSummary(pointer));
        absoluteAddresses.remove(pid);
    }

    private boolean joinPointers(FramePointer pointer){
        boolean hasJoined = false;
        for(int x = 0; x < openDataPoints.size(); x++){
            if(openDataPoints.get(x).getStart() == pointer.getEnd()){
                print("Joining pointers " + pointerSummary(openDataPoints.get(x)) + " and " + pointerSummary(pointer));
                pointer.setEnd(openDataPoints.get(x).getEnd());
                openDataPoints.remove(x);
                x--;
                hasJoined = true;
            } else if(openDataPoints.get(x).getEnd() == pointer.getStart()){
                print("Joining pointers " + pointerSummary(openDataPoints.get(x)) + " and " + pointerSummary(pointer));
                pointer.setStart(openDataPoints.get(x).getStart());
                openDataPoints.remove(x);
                x--;
                hasJoined = true;
            }
        }
        openDataPoints.add(pointer);
        return hasJoined;
    }

    private String pointerSummary(FramePointer pointer){
        return pointer.getStart() + " to " + pointer.getEnd();
    }

    private void print(String message){
        printQueue.add("[Memory Status] " + message);
    }

    private void printError(String errorMessage){
        dataFromMemoryToCPU.add(new Instruction(Opcode.ERR, new Operand[]{new Operand(new RuntimeException(errorMessage).toString(), AddressMode.IMMEDIATE)}));
    }

    private double getPercentUsage(){
        int openPoints = 0;
        for(FramePointer pointer: openDataPoints){
            openPoints += pointer.getBounds();
        }
        return (double)openPoints / (double)amountOfPages;
    }

    public Instruction[][] getMemoryContents(){
        Instruction[][] memoryContents = new Instruction[pageSize][amountOfPages];
        for(int x = 0; x < amountOfPages; x++){
            memoryContents[x] = memoryChip.getData(x);
        }
        return memoryContents;
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
