package Memory.ram;


import DataTypes.SynchronisedQueue;
import Memory.Pointers.PagePointer;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.Instruction.Instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryController extends Thread{
    private MemoryChip memoryChip;
    private int pageSize;
    private int amountOfPages;
    private Map<Integer, PagePointer> absoluteAddresses = new HashMap<>();
    private ArrayList<PagePointer> openDataPoints = new ArrayList<>();
    private SynchronisedQueue<Instruction> dataQueueToCPU;
    private SynchronisedQueue<Instruction> dataQueueToMemory;
    private SynchronisedQueue<Address> addressQueue;
    private SynchronisedQueue printQueue;
    private boolean computerIsRunning;

    public MemoryController(SynchronisedQueue<Instruction> dataQueueToCPU, SynchronisedQueue<Instruction> dataQueueToMemory,
                            SynchronisedQueue<Address> addressQueue, SynchronisedQueue printQueue, int amountOfPages,
                            int pageSize, boolean computerIsRunning){
        this.amountOfPages = amountOfPages;
        this.pageSize = pageSize;
        this.memoryChip = new MemoryChip(amountOfPages, pageSize);
        this.dataQueueToCPU = dataQueueToCPU;
        this.dataQueueToMemory = dataQueueToMemory;
        this.addressQueue = addressQueue;
        this.computerIsRunning = computerIsRunning;
        this.openDataPoints.add(new PagePointer(0, amountOfPages));
        this.printQueue = printQueue;
    }

    public void run(){
        while(computerIsRunning){
            final Address relativeAddress = addressQueue.remove();
            if(relativeAddress.getAddress() == -1){
                deleteData(relativeAddress.getPid());
            } else{
                int[] absoluteAddress;
                if(dataQueueToMemory.size() > 0){
                    Instruction instruction = dataQueueToMemory.remove();
                    if(instruction.getProcess() == Opcode.HDR){
                        absoluteAddress = createProgramSpace(relativeAddress.getPid(), instruction.getArg(0).getIntArgument());
                    }else{
                        absoluteAddress = getAbsoluteAddress(relativeAddress);
                    }
                    if(absoluteAddress[0] == -1){
                        printError("Memory failed to store data, due to low available storage or missing process identification");
                    }else{
                        Instruction[] page = memoryChip.getData(absoluteAddress[0]);
                        page[absoluteAddress[1]] = instruction;
                        memoryChip.setData(absoluteAddress[0], page);
                    }
                } else{
                    absoluteAddress = getAbsoluteAddress(relativeAddress);
                    if(absoluteAddress[0] == -1){
                        printError("Memory address requested is out of bounds of program space");
                        dataQueueToCPU.add(new Instruction(Opcode.ERR, null));
                    }else {
                        Instruction[] page = memoryChip.getData(absoluteAddress[0]);
                        dataQueueToCPU.add(page[absoluteAddress[1]]);
                    }
                }
            }
        }
    }

    public int decodeAddress(int address){
        int pageNum = address / pageSize;
        System.out.println(pageNum);
        return ++pageNum;
    }

    private int[] getAbsoluteAddress(Address relativeAddress){
        if(absoluteAddresses.containsKey(relativeAddress.getPid())){
            int pageNeeded = decodeAddress(relativeAddress.getAddress());
            return new int[]{pageNeeded, relativeAddress.getAddress()%pageSize};
        }
        return new int[]{-1};
    }

    private int[] createProgramSpace(int pid, int spaceSize) {
        int pagesNeeded = decodeAddress(spaceSize);
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
        return new int[]{-1};
    }

    private PagePointer splitPointer(PagePointer pointer, int size){
        final int end = pointer.getStart()+size;
        PagePointer finalPointer = new PagePointer(pointer.getStart(), end);
        pointer.setStart(end);
        return finalPointer;
    }

    private void deleteData(int pid){
        PagePointer pointer = absoluteAddresses.get(pid);
        if(!joinPointers(pointer)) openDataPoints.add(pointer);
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
        dataQueueToCPU.add(new Instruction(Opcode.ERR, new Operand[]{new Operand(new RuntimeException(errorMessage).toString(), AddressMode.NONE)}));
    }

    @Override
    public String toString() {
        StringBuffer memorySummary = new StringBuffer();

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
