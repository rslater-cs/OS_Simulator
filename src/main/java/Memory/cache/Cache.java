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

    private SynchronisedQueue<Instruction> dataToCPU;
    private SynchronisedQueue<Instruction> dataFromMemory;

    private SynchronisedQueue<Address> addressToMemory;
    private SynchronisedQueue<Address> addressFromCPU;

    public Cache(SynchronisedQueue<Instruction> dataToCPU, SynchronisedQueue<Instruction> dataFromMemory,
                 SynchronisedQueue<PCB> processesToBeRun, int cacheSize){
        this.instructions = new Instruction[cacheSize];
        this.addresses = new Address[cacheSize];
        this.pointer = new FreeCellPointer(cacheSize);
        this.dataFromMemory = dataFromMemory;
        this.dataToCPU = dataToCPU;
        this.processesToBeRun = processesToBeRun;
    }

    public void run(){
        while(true){
            if(processesToBeRun.size() > 0 && pointer.isFreeSpace()){
                PCB pcb = processesToBeRun.remove();
                fetchData(pcb.getProgramCounter(), pcb.getQuantum(), pcb.getID());
            }
            if(addressFromCPU.size() > 0){
                Address address = addressFromCPU.remove();
                int index;
                if((index = addressExists(address)) != -1){
                    dataToCPU.add(instructions[index]);
                    pointer.addFreePoint(index);
                }else{
                    dataToCPU.add(memoryFetch(address));
                }
            }
        }
    }

    private void fetchData(int start, int length, int pid){
        for(int x = 0; x < length; x++){
            final int index = pointer.nextFreeSpace();
            addresses[index] = new Address(pid, x+start);
            instructions[index] = memoryFetch(addresses[index]);
        }
    }

    private Instruction memoryFetch(Address address){
        addressToMemory.add(address);
        try {
            Thread.sleep(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataFromMemory.remove();
    }

    private int addressExists(Address address){
        for(int x = 0; x < addresses.length; x++){
             if(addresses[x].equals(address)) return x;
        }
        return -1;
    }
}
