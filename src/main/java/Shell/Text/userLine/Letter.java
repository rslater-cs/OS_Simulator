package Shell.Text.userLine;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Letter {
    private GridPane letterGrid = new GridPane();
    private Text letter;
    private static final Rectangle cursorSymbol = new Rectangle(2, 10);

    public Letter(String letter){
        cursorSymbol.setFill(Color.WHITE);
        this.letter = new Text(letter);
        this.letterGrid.add(this.letter, 0, 0);
        setType(LetterType.NORMAL);
    }

    public void setFocus(boolean isFocus){
        if(isFocus){
            this.letterGrid.add(cursorSymbol, 2, 0);
        }else{
            if(this.letterGrid.getChildren().size() == 2){
                this.letterGrid.getChildren().remove(1);
            }
        }
    }

    public void setType(LetterType letterType){
        this.letter.setFill(ColorDecoder.getColor(letterType));
    }

    public String getLetter(){
        return letter.getText();
    }

    public GridPane getRender(){
        return letterGrid;
    }

    @Override
    public String toString(){
        return letter.getText();
    }
}
