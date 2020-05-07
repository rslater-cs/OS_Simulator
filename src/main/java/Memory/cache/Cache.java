package Memory.cache;

import DataTypes.SynchronisedQueue;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.ProcessControlBlock.PCB;

public class Cache extends Thread{
    private FreeCellPointer pointer;

    private Instruction[] instructions;
    private Address[] addresses;

    private SynchronisedQueue<PCB> processesToBeRun;

    private SynchronisedQueue<Instruction> dataFromCacheToCPU;
    private SynchronisedQueue<Instruction> dataFromMemoryToCache;

    private SynchronisedQueue<Address> addressFromCacheToMemory;
    private SynchronisedQueue<Address> addressFromCPUToCache;

    private boolean computerIsRunning = true;

    public Cache(SynchronisedQueue<Instruction> dataFromCacheToCPU,
                 SynchronisedQueue<Instruction> dataFromMemoryToCache,
                 SynchronisedQueue<Address> addressFromCacheToMemory,
                 SynchronisedQueue<Address> addressFromCPUToCache,
                 SynchronisedQueue<PCB> processesToBeRun,
                 int cacheSize){
        this.instructions = new Instruction[cacheSize];
        this.addresses = new Address[cacheSize];

        this.pointer = new FreeCellPointer(cacheSize);

        this.dataFromMemoryToCache = dataFromMemoryToCache;
        this.dataFromCacheToCPU = dataFromCacheToCPU;

        this.addressFromCacheToMemory = addressFromCacheToMemory;
        this.addressFromCPUToCache = addressFromCPUToCache;

        this.processesToBeRun = processesToBeRun;
    }

    public void run(){
        while(computerIsRunning){
            if(processesToBeRun.size() > 0 && pointer.isFreeSpace()){
                PCB pcb = processesToBeRun.remove();
                System.out.println(pcb);
                fetchData(pcb.getProgramCounter(), pcb.getQuantum(), pcb.getID());
            }
            if(addressFromCPUToCache.size() > 0){
                Address address = addressFromCPUToCache.remove();
                System.out.println(address);
                int index;
                if((index = addressExists(address)) != -1){
                    dataFromCacheToCPU.add(instructions[index]);
                    pointer.addFreePoint(index);
                }else{
                    dataFromCacheToCPU.add(memoryFetch(address));
                }
            }
        }
    }

    public void endThread(){
        computerIsRunning = false;
    }

    private void fetchData(int start, int length, int pid){
        for(int x = 0; x < length; x++){
            final int index = pointer.nextFreeSpace();
            System.out.println(index);
            addresses[index] = new Address(pid, x+start);
            instructions[index] = memoryFetch(addresses[index]);
        }
    }

    private Instruction memoryFetch(Address address){
        addressFromCacheToMemory.add(address);
        return dataFromMemoryToCache.remove();
    }

    private int addressExists(Address address){
        for(int x = 0; x < addresses.length; x++){
             if(addresses[x].equals(address)) return x;
        }
        return -1;
    }
}
