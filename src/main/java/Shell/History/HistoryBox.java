package Shell.History;

import Shell.Text.userLine.TextBox;

import java.util.ArrayList;

public class HistoryBox {
    private ArrayList<TextBox> lines = new ArrayList<>();
    private int historyPointer = -1;

    public void addHistory(TextBox line){
        if(line != null){
            lines.remove(line);
            lines.add(line);
            historyPointer = lines.size();
        }
    }

    public TextBox incHistory(){
        if(historyPointer == -1){
            return null;
        }
        if(historyPointer > 0){
            return lines.get(--historyPointer);
        }
        return lines.get(0);
    }

    public TextBox decHistory(){
        if(historyPointer < lines.size()-1){
            return lines.get(++historyPointer);
        }
        return null;
    }
}
