package Shell;

import Shell.Text.Cursor.Cursor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Shell extends Application {
    private Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    private Cursor cursor = new Cursor();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
