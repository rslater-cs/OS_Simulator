package Shell;

import Shell.Text.MessageBox;
import Shell.Text.Validater.Validation;
import Shell.Text.userLine.TextBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Shell extends Application {
    private static final Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    private static final double COMMAND_LINE_HEIGHT = 20;
    private TextBox userRegion = new TextBox(background, COMMAND_LINE_HEIGHT);

    private static final String TITLE = "OS Simulator";

    private static final double OUTPUT_TEXT_HEIGHT = 500;
    private static final double OUTPUT_TEXT_WIDTH = 900;
    private MessageBox textView = new MessageBox(OUTPUT_TEXT_HEIGHT, OUTPUT_TEXT_WIDTH, background);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane grid = new GridPane();
        grid.add(textView.getRender(), 0, 0);
        grid.add(userRegion.getRender(), 0, 1);
        Scene scene = new Scene(grid, OUTPUT_TEXT_WIDTH, OUTPUT_TEXT_HEIGHT+COMMAND_LINE_HEIGHT);
        scene.setFill(Color.BLACK);

        scene.setOnKeyPressed(this::decode);

        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

    private void decode(KeyEvent e){
        String letter = "";

        final KeyCode code = e.getCode();
        boolean enter = false;

        if(code.isDigitKey()){
            if(e.isShiftDown()){
                letter = Validation.numberRowSymbol(e.getText());
            }else{
                letter = e.getText();
            }
            enter = true;
        } else if(code.isWhitespaceKey()){
            if(code == KeyCode.ENTER){
               textView.addMessage(userRegion.toString());
               userRegion.reset();
            } else {
                letter = " ";
                enter = true;
            }
        } else if(code == KeyCode.BACK_SPACE){
            userRegion.deleteLast();
        }else if(!isSpecialKey(code)){
            if(e.isShiftDown()){
                letter = e.getText().toUpperCase();
            }else {
                letter = e.getText();
            }
            enter = true;
        }

        if(enter) {
            userRegion.add(letter);
        }
    }

    private boolean isSpecialKey(KeyCode code){
        return code.isFunctionKey() || code.isNavigationKey() || code.isModifierKey() || code.isMediaKey() || code.isKeypadKey() || code.isArrowKey();
    }
}
