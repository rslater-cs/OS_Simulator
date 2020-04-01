package FileHandler;

import ProcessFormats.ProcessLine;
import Schedulers.RoundRobin.RoundRobin;

import java.util.ArrayList;

public class FileHandler {
    ArrayList<CodeReader> files = new ArrayList<>();
    RoundRobin roundRobin;

    public FileHandler(RoundRobin roundRobin){
        this.roundRobin = roundRobin;
    }

    public void startFile(String path){
        if(path == null) throw new IllegalArgumentException("Null file passed");
        int PID = roundRobin.getNextAvailableID();
        roundRobin.addProcess(PID);
        files.add(PID, new CodeReader(path));
    }

    public void exitFile(int PID){
        files.remove(PID);
        roundRobin.removeProcess(PID);
    }

    public ProcessLine getLine(){
        int PID = roundRobin.getProcess();
        return new ProcessLine(PID, files.get(PID).getLine());
    }
}
