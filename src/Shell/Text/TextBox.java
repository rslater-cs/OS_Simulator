package Shell.Text;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class TextBox {
    private ArrayList<Word> words = new ArrayList<>();
    private GridPane textBox = new GridPane();

    public TextBox(){
        textBox.setOnKeyPressed(this::keyAction);
    }

    private void keyAction(KeyEvent event){
        //TODO MAKE KEY EVENT FOR LEFT AND RIGHT CURSOR;
    }

}
