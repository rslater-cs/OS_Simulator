package FileHandler;

import ProcessFormats.Opcode.Opcode;
import ProcessFormats.Process.ProcessLine;
import Schedulers.RoundRobin.RoundRobin;

import java.util.ArrayList;
import java.util.Queue;

public class FileHandler {
    private ArrayList<FileReader> files = new ArrayList<>();
    private RoundRobin roundRobin;
    private Queue<Opcode> fileRequests;
    private Queue<Opcode> instructions;

    private static final String BOOT_INIT = "boot";
    private static final String CODE_INIT = ""

    public FileHandler(Queue<Opcode> fileRequests, Queue<Opcode> instructions){
        this.fileRequests = fileRequests;
        this.instructions = instructions;
        this.roundRobin = new RoundRobin(1);
    }

    public void endFile(int PID){
        files.remove(PID);
        roundRobin.removeProcess(PID);
    }

    public ProcessLine getLine(){
        int PID = roundRobin.getProcess();
        return new ProcessLine(PID, files.get(PID).getLine());
    }

    public void processReceiver(){
        while(fileRequests.size() == 0){
            //wait
        }
        while(fileRequests.size() > 0){
            readFile(fileRequests.remove());
        }

    }

    private void readFile(Opcode fileRequest){
        final FileReader fileReader = new FileReader(fileRequest.getArgs()[0]);
        String initLine = fileReader.getLine();
    }
}
