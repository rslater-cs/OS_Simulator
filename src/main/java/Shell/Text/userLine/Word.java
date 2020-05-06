package Shell.Text.userLine;


import Shell.Text.Validater.Validation;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Word {
    private ArrayList<Letter> letters = new ArrayList<>();
    private GridPane word = new GridPane();

    public Word(String letter){
        addLetter(letter);
    }

    public void addLetter(String letter){
        Letter text = new Letter(letter);
        word.add(text.getRender(), letters.size(), 0);
        this.letters.add(letters.size(), text);
        //setFocusForward();
    }

    private void setFocusForward(){
        if(letters.size() > 0) {
            letters.get(letters.size() - 1).setFocus(true);
            if(letters.size() > 1){
                letters.get(letters.size()-2).setFocus(false);
            }
        }
    }

    private void move(GridPane letter, int index){
        for(int x = letters.size()-1; x > index; x--){
            word.add(word.getChildren().get(x), x+1, 0);
            word.getChildren().remove(x);
        }
        word.add(letter, index, 0);
    }

    public boolean deleteLast(){
        if(letters.size() > 0){
            letters.remove(letters.size()-1);
            word.getChildren().remove(letters.size());
            //setFocusForward();
            return true;
        }
        return false;
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
        letters.get(letters.size()-1).setFocus(false);
        setType(Validation.validateWord(getWord()));
    }

    public String getLastLetter(){
        if(letters.size() == 0){
            return "";
        }
        return letters.get(letters.size()-1).getLetter();
    }

    public GridPane getRender(){
        return word;
    }

    @Override
    public String toString(){
        StringBuffer letterSummary = new StringBuffer();

        for(Letter letter : letters){
            letterSummary.append(letter);
        }

        return letterSummary.toString();
    }
}
