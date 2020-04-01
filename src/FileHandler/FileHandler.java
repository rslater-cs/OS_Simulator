package FileHandler;

import java.util.ArrayList;

public class FileHandler {
    ArrayList<CodeReader> files = new ArrayList<>();

    public void addFile(String path){
        files.add(new CodeReader(path));
    }

    public void endFile(int PID){
        
    }
}
