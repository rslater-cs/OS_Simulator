package Shell.Text.OutputDisplay;


import DataTypes.SynchronisedQueue;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class MessageBox {
    private String text = "";
    private Label textArea = new Label();
    private ScrollPane scrollPane = new ScrollPane();
    private SynchronisedQueue<String> printQueue;

    public MessageBox(double height, double width, Background background, SynchronisedQueue<String> printQueue){
        textArea.setMinHeight(height);
        textArea.setMinWidth(width);
        textArea.setBackground(background);
        textArea.setTextFill(Color.WHITE);
        textArea.setAlignment(Pos.BOTTOM_LEFT);
        scrollPane.setContent(textArea);
        scrollPane.setMinHeight(height);
        scrollPane.setMinWidth(width);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        textArea.setText(text);
        this.printQueue = printQueue;

        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            public void run() {
                while(true){
                    text += "\n" + printQueue.remove();
                    updateMessage(text);
                }
            }
        };

        textArea.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    public StringProperty getTextProperty(){
        return textArea.textProperty();
    }

    public void addMessage(String message){
        text += "\n" + message;
    }

    public ScrollPane getRender(){
        return scrollPane;
    }
}
