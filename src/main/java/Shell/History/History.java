package Shell.History;

import Shell.Text.userLine.TextBox;

import java.util.ArrayList;

public class History {
    private ArrayList<TextBox> lines = new ArrayList<>();
    private int historyPointer = 0;

    public void addHistory(TextBox line){
        if(line != null){
            lines.remove(line);
            lines.add(line);
            historyPointer = lines.size();
        }
    }

    public TextBox incHistory(TextBox region){
        if(lines.size() == 0){
            return region;
        }
        if(historyPointer > 0){
            return lines.get(--historyPointer);
        }
        return lines.get(0);
    }

    public TextBox decHistory(TextBox region){
        if(historyPointer < lines.size()-1){
            return lines.get(++historyPointer);
        }
        return region;
    }
}
