package DataTypes;

import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.MemoryAddress.Address;

public class DataAddressPacket {
    private Address address;
    private Instruction instruction;

    public DataAddressPacket(Address address, Instruction instruction){
        this.address = address;
        this.instruction = instruction;
    }

    public Address getAddress(){
        return address;
    }

    public Instruction getInstruction(){
        return instruction;
    }
}
