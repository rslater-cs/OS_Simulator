package Shell;

import DataTypes.SynchronisedArrayList;
import DataTypes.SynchronisedQueue;
import Memory.MemoryController;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.Data.Opcode.Opcode;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import Scheduler.LongTermScheduler;
import Scheduler.ShortTermScheduler;
import Shell.CommandExecuter.Executer;
import Shell.History.HistoryBox;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Shell extends Application {
    private static final Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    private static final double COMMAND_LINE_HEIGHT = 20;
    private TextBox userRegion = new TextBox(background, COMMAND_LINE_HEIGHT);

    private static final String TITLE = "OS Simulator";

    private static final double OUTPUT_TEXT_HEIGHT = 500;
    private static final double OUTPUT_TEXT_WIDTH = 900;
    private MessageBox textView = new MessageBox(OUTPUT_TEXT_HEIGHT, OUTPUT_TEXT_WIDTH, background);

    private HistoryBox history = new HistoryBox();

    GridPane grid = new GridPane();

    private static final double PADDING = 10;

    private static final Rectangle cursor = new Rectangle(2, 15);

    private Executer executer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        makeStage(stage);
        makeComponents();
    }

    private void makeStage(Stage stage){
        cursor.setFill(Color.WHITE);
        GridPane gridPane = makeCursorGrid();
        grid.add(textView.getRender(), 0, 0);
        grid.add(gridPane, 0, 1);

        Scene scene = new Scene(grid, OUTPUT_TEXT_WIDTH, OUTPUT_TEXT_HEIGHT+COMMAND_LINE_HEIGHT);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(this::decode);

        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

    private void makeComponents(){
        SynchronisedQueue<Address> addressQueue = new SynchronisedQueue<>(1);
        SynchronisedQueue<Opcode> dataToCPU = new SynchronisedQueue<>(1);
        SynchronisedQueue<Opcode> dataToMemory = new SynchronisedQueue<>(1);

        SynchronisedQueue<PCB> jobQueue = new SynchronisedQueue<>(1000);
        SynchronisedArrayList<PCB> sortedQueue = new SynchronisedArrayList<>(100);
        SynchronisedQueue<PCB> readyQueue = new SynchronisedQueue<>(10);

        LongTermScheduler longScheduler = new LongTermScheduler(jobQueue, sortedQueue, true, 10, 5);
        ShortTermScheduler shortScheduler = new ShortTermScheduler(sortedQueue, readyQueue, true);

        SynchronisedQueue<String> printQueue = new SynchronisedQueue<>(100);

        CPU processor = new CPU(readyQueue, jobQueue, addressQueue, dataToCPU, dataToMemory, printQueue,  1, true);
        executer = new Executer(processor, addressQueue, dataToMemory, jobQueue);
        MemoryController ram = new MemoryController(dataToCPU, dataToMemory, addressQueue, 35, true);
        ram.start();
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
               Exception exception = executer.start(userRegion.toString());
               if(exception != null){
                   textView.addMessage(exception.toString());
               }
               history.addHistory(userRegion);
               userRegion = new TextBox(background, COMMAND_LINE_HEIGHT);
               changeText();
            } else {
                letter = " ";
                enter = true;
            }
        } else if(code == KeyCode.BACK_SPACE){
            userRegion.deleteLast();
        }else if (code.isArrowKey()){
            if(code == KeyCode.UP){
                userRegion = history.incHistory();
                changeText();
            } else if(code == KeyCode.DOWN){
                TextBox temp = history.decHistory();
                if(temp != null) {
                    userRegion = temp;
                }
                else{
                    userRegion = new TextBox(background, COMMAND_LINE_HEIGHT);
                }
                changeText();
            }
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

    private void changeText(){
        grid.getChildren().remove(1);
        grid.add(makeCursorGrid(), 0, 1);
    }

    private GridPane makeCursorGrid(){
        GridPane gridPane = new GridPane();
        gridPane.setBackground(background);
        gridPane.add(userRegion.getRender(), 0, 0);
        gridPane.add(cursor, 1, 0);
        return gridPane;
    }

    private boolean isSpecialKey(KeyCode code){
        return code.isFunctionKey() || code.isNavigationKey() || code.isModifierKey() || code.isMediaKey() || code.isKeypadKey() || code.isArrowKey();
    }
}
