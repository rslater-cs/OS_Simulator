package Shell.CommandExecuter;

import Memory.instruction.cache.Cache;
import Memory.ram.MemoryController;
import Processor.CPU;
import Scheduler.LongTermScheduler;
import Scheduler.ShortTermScheduler;
import Shell.Text.OutputDisplay.MessageBox;

public class ThreadExecutioner {
    private CPU processor;
    private MemoryController ram;
    private Cache cache;
    private ShortTermScheduler sts;
    private LongTermScheduler lts;
    private MessageBox output;

    public ThreadExecutioner(CPU processor, MemoryController ram, Cache cache,
                             ShortTermScheduler sts, LongTermScheduler lts){
        this.processor = processor;
        this.ram = ram;
        this.cache = cache;
        this.sts = sts;
        this.lts = lts;
    }

    public void addMessageBox(MessageBox messageBox){
        if(messageBox != null) this.output = messageBox;
    }

    public void endProgram(){
        processor.endThread();
        ram.endThread();
        cache.endThread();
        sts.endThread();
        lts.endThread();
        if(output != null) output.endThread();

        System.out.println("All threads put into end mode");

        try{
            processor.join();
            System.out.println("Processor shutdown successfully");
            ram.join();
            System.out.println("Memory shutdown successfully");
            cache.join();
            System.out.println("Cache shutdown successfully");
            sts.join();
            System.out.println("Short Term Scheduler shutdown successfully");
            lts.join();
            System.out.println("Long Term Scheduler shutdown successfully");
            System.out.println("Successful shutdown");
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }
    }
}
