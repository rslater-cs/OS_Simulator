package Memory.cache;

import DataTypes.SynchronisedQueue;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.MemoryAddress.Address;

public class Cache {
    private Instruction[] instructions;
    private Address[] addresses;
    private SynchronisedQueue<Instruction> dataToCPU;
    private SynchronisedQueue<Address> dataFromCPU;

    public Cache(SynchronisedQueue<Instruction> dataToCPU, SynchronisedQueue<Address> dataFromCPU, int cacheSize){
        this.instructions = new Instruction[cacheSize];
        this.addresses = new Address[cacheSize];
        this.dataFromCPU = dataFromCPU;
        this.dataToCPU = dataToCPU;
    }
}
