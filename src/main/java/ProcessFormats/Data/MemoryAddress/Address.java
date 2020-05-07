package ProcessFormats.Data.MemoryAddress;

public class Address {
    private int pid;
    private int address;

    public Address(int pid, int address){
        this.pid = pid;
        this.address = address;
    }

    public int getPid() {
        return pid;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object obj) {
        Address otherAddress = (Address)obj;
        return (otherAddress.getPid() == getPid() && otherAddress.getAddress() == getAddress());
    }
}
