package Shell.Text.Cursor;

public class Cursor {
    private int letter = 0;

    public void inc(){
        letter++;
    }

    public void dec(){
        if(letter < 0) letter--;
    }

    public void reset(){
        letter = 0;
    }

    public int getLetterIndex(){
        return letter;
    }
}
