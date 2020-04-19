package Shell.Text.userLine;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class TextBox {
    private ArrayList<Word> words = new ArrayList<>();
    private GridPane textBox = new GridPane();


    public TextBox(Background background, double minHeight){
        this.textBox.setBackground(background);
        this.textBox.setMinHeight(minHeight);
    }

    public void add(String letter){
        if(getLastLetter().equals(" ") || letter.equals(" ") || words.size() == 0){
            if(words.size() != 0) {
                lastWord().endWord();
            }
            Word word = new Word(letter);
            words.add(word);
            textBox.add(word.getRender(), words.size(), 0);
        }else {
            lastWord().addLetter(letter);
        }
    }

    private String getLastLetter(){
        final Word word = lastWord();
        if(word == null){
            return "";
        }
        return word.getLastLetter();
    }

    private Word lastWord(){
        if(words.size() == 0){
            return null;
        }
        return words.get(words.size()-1);
    }

    public GridPane getRender(){
        return textBox;
    }

    public void deleteLast(){
        if(words.size() > 0){
            if(!lastWord().deleteLast()){
                delete(words.size()-1);
                deleteLast();
            }
        }
    }

    private void delete(int index){
        words.remove(index);
        textBox.getChildren().remove(index);
    }

    public void reset(){
        this.words.clear();
        this.textBox.getChildren().clear();
    }

    @Override
    public String toString(){
        StringBuffer wordSummary = new StringBuffer();
        for(Word word : words){
            wordSummary.append(word);
        }
        return wordSummary.toString();
    }

}
