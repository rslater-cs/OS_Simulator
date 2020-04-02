package FileHandler;

import java.io.File;
import java.util.Scanner;

public class FileReader {
    private String path;
    private Scanner file;

    public FileReader(String path){
        this.path = path;
        this.file = createScanner();
        if(file == null) throw new IllegalArgumentException("File creation failed");
    }

    public String getLine(){
        try {
            return file.nextLine();
        } catch(Exception e){
            return null;
        }
    }

    public int getFileLength(){
        int x = 0;
        while (file.hasNextLine()){
            x++;
            file.nextLine();
        }
        file = createScanner();
        return x;
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
