package Memory.data.secondarymemory;

import DataTypes.SynchronisedQueue;
import Memory.data.MemoryChip;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.Instruction.Opcode.Opcode;
import ProcessFormats.Data.Instruction.Operand.AddressMode;
import ProcessFormats.Data.Instruction.Operand.Operand;
import ProcessFormats.Data.MemoryAddress.Address;

public class VirtualMemoryController extends Thread{
    private SynchronisedQueue<Instruction[]> pageFromMemoryToVirtual;
    private SynchronisedQueue<Instruction[]> pageFromVirtualToMemory;
    private SynchronisedQueue<Address> addressFromMemoryToVirtual;
    private int pageAmount;
    private MemoryChip memoryChip;
    private boolean computerIsRunning = true;

    public VirtualMemoryController(SynchronisedQueue<Instruction[]> pageFromMemoryToVirtual,
                                   SynchronisedQueue<Address> addressFromMemoryToVirtual,
                                   SynchronisedQueue<Instruction[]> pageFromVirtualToMemory,
                                   int pageSize,
                                   int pageAmount){
        this.memoryChip = new MemoryChip(pageAmount, pageSize);
        this.pageAmount = pageAmount;
        this.pageFromMemoryToVirtual = pageFromMemoryToVirtual;
        this.pageFromVirtualToMemory = pageFromVirtualToMemory;
        this.addressFromMemoryToVirtual = addressFromMemoryToVirtual;
    }

    @Override
    public void run(){
        while(computerIsRunning){
            if(pageFromMemoryToVirtual.size() > 0){
                Instruction[] page = pageFromMemoryToVirtual.remove();
                int pageIndex = placePage(page);
                if(pageIndex == -1){
                    System.out.println("error vm full");
                }else {
                    pageFromVirtualToMemory.add(buildIndexMessage(pageIndex));
                }
            }
            if(addressFromMemoryToVirtual.size() > 0){
                pageFromVirtualToMemory.add(getPage(addressFromMemoryToVirtual.remove().getAddress()));
            }
        }
    }

    private int placePage(Instruction[] page){
        for(int x = 0; x < pageAmount; x++){
            if(memoryChip.getData(x)[0] == null){
                memoryChip.setData(x, page);
                return x;
            }
        }
        return -1;
    }

    private Instruction[] buildIndexMessage(int pageIndex){
        return new Instruction[]{new Instruction(Opcode.HDR,
                new Operand[]{
                        new Operand(pageIndex, AddressMode.IMMEDIATE)
        })};
    }

    private Instruction[] getPage(int pageNumber){
        final Instruction[] page = memoryChip.getData(pageNumber);
        Instruction[] clearPage = page.clone();
        clearPage[0] = null;
        memoryChip.setData(pageNumber, clearPage);
        return page;
    }

    public void endThread(){
        this.computerIsRunning = false;
    }
}
