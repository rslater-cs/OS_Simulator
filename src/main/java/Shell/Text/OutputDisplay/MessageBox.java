package Shell.Text.OutputDisplay;


import datatypes.SynchronisedQueue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class MessageBox {
    private Label text = new Label("");
    private ScrollPane textArea = new ScrollPane();
    private boolean computerIsRunning = true;

    public MessageBox(double height, double width, Background background, SynchronisedQueue<String> printQueue){
        textArea.setMinHeight(height);
        textArea.setMinWidth(width);
        text.setMinHeight(height);
        text.setMinWidth(width);
        textArea.setBackground(background);
        text.setBackground(background);
        textArea.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        textArea.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        text.setTextFill(Color.WHITE);
        text.setAlignment(Pos.BOTTOM_LEFT);
        textArea.setContent(text);

        Task<Void> task = new Task<>(){

            @Override
            protected Void call() throws Exception {
                while(computerIsRunning) {
                    textArea.setVvalue(1.0);
                    final String newText =  printQueue.remove();
                    final String message = text.getText() + newText + "\n";
                    updateMessage(message);
                    Thread.sleep(10);
                }
                return null;
            }
        };
        text.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    public ScrollPane getRender(){
        return textArea;
    }

    public void endThread(){
        computerIsRunning = false;
    }
}
