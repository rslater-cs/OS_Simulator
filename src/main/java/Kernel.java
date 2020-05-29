import datatypes.SynchronisedArrayList;
import datatypes.SynchronisedQueue;
import filehandler.FileReader;
import Memory.cache.Cache;
import Memory.ram.MemoryController;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import Scheduler.LongTermScheduler;
import Scheduler.ShortTermScheduler;
import Shell.CommandExecuter.ThreadExecutioner;
import Shell.Shell;
import Shell.subsystemstats.GraphData;
import Shell.subsystemstats.SubSystemGraph;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Kernel extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileReader bootFile = new FileReader("bootfile.txt");

        int[] bootDetails = new int[11];
        int index = 0;
        while(index < 11){
            try {
                String line = bootFile.getLine();
                bootDetails[index++] = Integer.parseInt(line.split(" ")[2]);
            }catch (Exception e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        SynchronisedQueue<Address> addressFromCPUToMemory = new SynchronisedQueue<>(bootDetails[0]);
        SynchronisedQueue<Address> addressFromCPUToCache = new SynchronisedQueue<>(bootDetails[0]);
        SynchronisedQueue<Address> addressFromCacheToMemory = new SynchronisedQueue<>(bootDetails[0]);

        SynchronisedQueue<Instruction> dataFromCPUToMemory = new SynchronisedQueue<>(bootDetails[0]);
        SynchronisedQueue<Instruction> dataFromMemoryToCPU = new SynchronisedQueue<>(bootDetails[0]);
        SynchronisedQueue<Instruction> dataFromCacheToCPU = new SynchronisedQueue<>(bootDetails[0]);
        SynchronisedQueue<Instruction> dataFromMemoryToCache = new SynchronisedQueue<>(bootDetails[0]);

        SynchronisedQueue<PCB> jobQueue = new SynchronisedQueue<>(bootDetails[1]);
        SynchronisedArrayList<PCB> sortedQueue = new SynchronisedArrayList<>(bootDetails[2]);
        SynchronisedQueue<PCB> readyQueue = new SynchronisedQueue<>(bootDetails[3]);
        SynchronisedQueue<PCB> jobsToBeRun = new SynchronisedQueue<>(bootDetails[3]);

        SynchronisedQueue<String> printQueue = new SynchronisedQueue<>(bootDetails[4]);

        ArrayList<GraphData> graphData = new ArrayList<>();

        SubSystemGraph graph = new SubSystemGraph(graphData);

        LongTermScheduler longScheduler = new LongTermScheduler(jobQueue, sortedQueue, bootDetails[5], bootDetails[6]);
        ShortTermScheduler shortScheduler = new ShortTermScheduler(sortedQueue, readyQueue, jobsToBeRun);

        Cache cache = new Cache(dataFromCacheToCPU, dataFromMemoryToCache, addressFromCacheToMemory,
                addressFromCPUToCache, jobsToBeRun, bootDetails[7]);

        MemoryController ram = new MemoryController(dataFromMemoryToCPU, dataFromCPUToMemory, dataFromMemoryToCache,
                addressFromCacheToMemory, addressFromCPUToMemory, printQueue, graphData, bootDetails[8], bootDetails[9]);

        CPU processor = new CPU(readyQueue, jobQueue, addressFromCPUToMemory, addressFromCPUToCache,
                dataFromMemoryToCPU, dataFromCPUToMemory, dataFromCacheToCPU, printQueue,  bootDetails[10]);

        cache.start();
        ram.start();
        shortScheduler.start();
        longScheduler.start();
        processor.start();

        ThreadExecutioner threadExecutioner = new ThreadExecutioner(processor, ram, cache, shortScheduler, longScheduler);


        Shell shell = new Shell(processor, addressFromCPUToMemory, dataFromCPUToMemory, jobQueue, printQueue, graph, threadExecutioner);
        primaryStage.setScene(shell.renderScene());

        String title = bootFile.getLine().split("=")[1];
        primaryStage.setTitle(title);
        primaryStage.show();
    }
}
