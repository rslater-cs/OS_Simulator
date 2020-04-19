package Shell.Text;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class MessageBox {
    private Label textArea = new Label();

    public MessageBox(double height, double width, Background background){
        textArea.setMinHeight(height);
        textArea.setMinWidth(width);
        textArea.setBackground(background);
        textArea.setTextFill(Color.WHITE);
        textArea.setAlignment(Pos.BOTTOM_LEFT);
    }

    public void addMessage(String message){
        textArea.setText(textArea.getText() + "\n" + message);
    }

    public Label getRender(){
        return textArea;
    }
}
