package Shell.Text;


import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Word {
    private ArrayList<Letter> letters = new ArrayList<>();

    public Word(Letter letter){
        letters.add(letter);
    }

    public void addLetter(Letter letter){
        letters.add(letter);
    }

    public Letter getLetter(int index){
        if(checkIndex(index)) return letters.get(index);
        throw new IndexOutOfBoundsException();
    }

    public void removeLetter(int index){
        if(checkIndex(index)) letters.remove(index);
    }

    public void setType(int index, LetterType letterType){
        if(checkIndex(index)){
            letters.get(index).setType(letterType);
        }
    }

    public void allType(LetterType letterType){
        for(Letter letter : letters){
            letter.setType(letterType);
        }
    }

    public int height(){
        return letters.get(0).getSize();
    }

    public int length(){
        return letters.get(0).getSize() * letters.size();
    }

    private boolean checkIndex(int index){
        return (index < letters.size());
    }

    private int getSize(){
        return letters.size();
    }

    public GridPane render(){
        GridPane grid = new GridPane();
        grid.setVgap(0);
        grid.setHgap(0);

        for(int x = 0; x < letters.size(); x++){
            grid.add(letters.get(x).render(), x, 0);
        }

        return grid;
    }
}
