package Shell.Text;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Letter {
    private Text letter;
    private LetterType type;
    private static final int size = 10;
    private boolean isFocus = true;
    private static final Rectangle cursor = new Rectangle(1, size);

    public Letter(String letter){
        this.letter = new Text(size, size, letter);
        setType(LetterType.NORMAL);
    }

    public int getSize(){
        return size;
    }

    public String getLetter() {
        return letter.getText();
    }

    public LetterType getType() {
        return type;
    }

    public void setType(LetterType type) {
        this.type = type;
        this.letter.setFill(makeColour(type));
    }

    public void setFocus(boolean focus){
        isFocus = focus;
    }

    public GridPane render(){
        final GridPane grid = new GridPane();
        grid.add(letter, 0, 0);
        if(isFocus){
            cursor.setFill(Color.WHITE);
            grid.add(cursor, 1, 0);
        }
        return grid;
    }

    private Color makeColour(LetterType type){
        return switch (type) {
            case NORMAL -> Color.WHITE;
            case FILLED -> Color.GREEN;
            case UNKNOWN -> Color.RED;
        };
    }
}
