package Shell;

import datatypes.SynchronisedQueue;
import ProcessFormats.Data.Instruction.Instruction;
import ProcessFormats.Data.MemoryAddress.Address;
import ProcessFormats.ProcessControlBlock.PCB;
import Processor.CPU;
import Shell.CommandExecuter.Executor;
import Shell.CommandExecuter.ThreadExecutioner;
import Shell.History.History;
import Shell.Text.OutputDisplay.MessageBox;
import Shell.Text.Validater.Validation;
import Shell.Text.userLine.TextBox;
import Shell.subsystemstats.SubSystemGraph;
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


public class Shell {
    private static final Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    private static final double COMMAND_LINE_HEIGHT = 20;
    private TextBox userRegion = new TextBox(background, COMMAND_LINE_HEIGHT);

    private static final String TITLE = "OS Simulator";

    private static final double OUTPUT_TEXT_HEIGHT = 500;
    private static final double OUTPUT_TEXT_WIDTH = 900;
    private MessageBox textView;

    private SynchronisedQueue<String> printQueue;

    private History history = new History();

    GridPane grid = new GridPane();

    private static final Rectangle cursor = new Rectangle(2, 15);

    private Executor executor;

    private Scene scene;

    public Shell(CPU processor,
                 SynchronisedQueue<Address> addressFromCPUToMemory,
                 SynchronisedQueue<Instruction> dataFromCPUToMemory,
                 SynchronisedQueue<PCB> jobQueue,
                 SynchronisedQueue<String> printQueue,
                 SubSystemGraph graph,
                 ThreadExecutioner threadExecutioner){
        this.printQueue = printQueue;
        makeComponents(processor, addressFromCPUToMemory, dataFromCPUToMemory, jobQueue, threadExecutioner, graph);
        makeScene();
    }

    private void makeScene(){
        cursor.setFill(Color.WHITE);
        GridPane gridPane = makeCursorGrid();
        grid.add(textView.getRender(), 0, 0);
        grid.add(gridPane, 0, 1);

        scene = new Scene(grid, OUTPUT_TEXT_WIDTH, OUTPUT_TEXT_HEIGHT+COMMAND_LINE_HEIGHT);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(this::decode);
    }

    public Scene renderScene(){
        return scene;
    }

    public MessageBox getMessageBox(){
        return textView;
    }

    private void makeComponents(CPU processor,
                                SynchronisedQueue<Address> addressFromCPUToMemory,
                                SynchronisedQueue<Instruction> dataFromCPUToMemory,
                                SynchronisedQueue<PCB> jobQueue,
                                ThreadExecutioner threadExecutioner,
                                SubSystemGraph graph){
        textView = new MessageBox(OUTPUT_TEXT_HEIGHT, OUTPUT_TEXT_WIDTH, background, printQueue);
        threadExecutioner.addMessageBox(textView);
        executor = new Executor(processor, addressFromCPUToMemory, dataFromCPUToMemory, jobQueue, graph, threadExecutioner);
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
               printQueue.add(userRegion.toString());
               Exception exception = executor.start(userRegion.toString());
               if(exception != null){
                   printQueue.add(exception.toString());
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
                userRegion = history.incHistory(userRegion);
                changeText();
            } else if(code == KeyCode.DOWN){
                userRegion = history.decHistory(userRegion);
                changeText();
            }
        }else if(!isSpecialKey(code)){
            if(e.isShiftDown()){
                if(code == KeyCode.SEMICOLON) letter = ":";
                else letter = e.getText().toUpperCase();
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
