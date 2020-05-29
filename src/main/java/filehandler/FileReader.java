package filehandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
    private String path;
    private Scanner file;

    public FileReader(String path){
        this.path = path;
        this.file = createScanner();
        if(file == null) throw new IllegalArgumentException("File cannot be found or accessed");
    }

    public String getLine(){
        try {
            return file.nextLine();
        } catch(Exception e){
            return null;
        }
    }

    public String[] getRest(){
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while((line = getLine()) != null){
            lines.add(line);
        }
        return lines.toArray(String[]::new);
    }

    private Scanner createScanner(){
        try{
            return new Scanner(new File(path));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
