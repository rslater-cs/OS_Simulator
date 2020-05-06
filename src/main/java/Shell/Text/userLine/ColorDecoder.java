package Shell.Text.userLine;

import javafx.scene.paint.Color;

public class ColorDecoder {

    public static Color getColor(LetterType letterType){
        return switch(letterType){
            case NORMAL -> Color.WHITE;
            case FUNCTION -> Color.GREEN;
            case DIRECTORY -> Color.LIGHTBLUE;
            case CONNECTOR -> Color.YELLOW;
            case VALUE -> Color.BLUEVIOLET;
            case BOOLEAN -> Color.CORAL;
            case UNKNOWN -> Color.RED;
        };
    }
}
