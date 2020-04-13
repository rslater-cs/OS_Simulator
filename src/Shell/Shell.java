package Shell;

import Shell.Text.Cursor.Cursor;
import Shell.Text.Letter;
import Shell.Text.Word;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Shell extends Application {
    private Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    private Cursor cursor = new Cursor();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Word word = new Word(new Letter("H"));
        word.addLetter(new Letter("e"));
        word.addLetter(new Letter("l"));
        word.addLetter(new Letter("l"));
        word.addLetter(new Letter("o"));
        word.addLetter(new Letter(" "));
        word.addLetter(new Letter("H"));
        word.addLetter(new Letter("e"));
        word.addLetter(new Letter("l"));
        word.addLetter(new Letter("l"));
        word.addLetter(new Letter("o"));

        for(int x = 0; x < 11; x++){
            word.getLetter(x).setFocus(false);
        }

        word.getLetter(5).setFocus(true);

        Group root = new Group();
        root.getChildren().add(word.render());

        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.show();
    }
}
