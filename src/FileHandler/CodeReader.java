package FileHandler;

import ProcessFormats.ProcessLine;

import java.io.File;
import java.util.Scanner;

public class CodeReader {
    private Scanner file;

    public CodeReader(String path){
        try {
            this.file = new Scanner(new File(path));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getLine(){
        try {
            return file.nextLine();
        } catch(Exception e){
            return null;
        }
    }
}
