package DataTypes;

import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.MemoryAddress.Address;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DataAddressQueue {
    private Queue<Address> addressQueue = new LinkedList<>();
    private Queue<Instruction> dataQueue = new LinkedList<>();
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int maxSize;

    public DataAddressQueue(int maxSize){
        this.maxSize = maxSize;
    }

    public void add(Address address, Instruction instruction){
        lock.lock();
        if(addressQueue.size() == maxSize){
            try {
                condition.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        addressQueue.add(address);
        dataQueue.add(instruction);
        condition.signal();
        lock.unlock();
    }

    public DataAddressPacket remove(){
        lock.lock();
        if(addressQueue.size() == 0){
            try {
                condition.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        final DataAddressPacket packet = new DataAddressPacket(addressQueue.remove(), dataQueue.remove());
        condition.signal();
        lock.unlock();
        return packet;
    }
}
