package Schedulers.RoundRobin;

import java.util.ArrayList;

public class RoundRobin {
    private ArrayList<Integer> processes = new ArrayList<>();
    private int currentProcess = -1;
    private LoopingNumber loopingNumber;

    public RoundRobin(int quantum){
         loopingNumber = new LoopingNumber(quantum);
    }

    public void addProcess(int PID){
        if(processes.contains(PID) || PID < 1) return;
        for(int x = 0; x < processes.size(); x++) {
            if (processes.get(x) > PID) {
                processes.add(x, PID);
                return;
            }
        }
        processes.add(PID);
    }

    public void removeProcess(int PID){
        if(processes.contains(PID)) processes.remove(Integer.valueOf(PID));
    }

    public int getProcess(){
        if(loopingNumber.hasLooped()) currentProcess = ++currentProcess % processes.size();
        return processes.get(currentProcess);
    }

    public int getNextAvailableID(){
        int PID = 1;
        for(int x = 0; x < processes.size(); x++){
            if(processes.get(x) != PID) return PID;
            PID++;
        }
        return PID;
    }
}
