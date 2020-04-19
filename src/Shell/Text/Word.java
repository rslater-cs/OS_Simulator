package Shell.Text;


import Shell.Text.Validater.Validation;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Word {
    private ArrayList<Letter> letters = new ArrayList<>();
    private GridPane word = new GridPane();
    private int size = 0;

    public Word(String letter){
        addLetter(letter, 0);
    }

    public void addLetter(String letter, int index){
        Letter text = new Letter(letter);
        this.letters.add(index, text);
        move(text.getRender(), index);
    }

    private void move(GridPane letter, int index){
        for(int x = size-1; x > index; x--){
            word.add(word.getChildren().get(x), x+1, 0);
            word.getChildren().remove(x);
        }
        word.add(letter, index, 0);
        size++;
    }

    public void deleteLetter(int index){
        if(letters.size() > index){
            letters.remove(index);
        }
    }

    public String getWord(){
        StringBuffer word = new StringBuffer();
        for(Letter letter : letters){
            word.append(letter);
        }
        return word.toString();
    }

    public void setType(LetterType letterType){
        for(Letter letter : letters){
            letter.setType(letterType);
        }
    }

    public void endWord(){
        setType(Validation.validateWord(getWord()));
    }
}
