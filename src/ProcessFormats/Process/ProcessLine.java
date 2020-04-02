package ProcessFormats.Process;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Pattern;

public class ProcessLine {
    Stack<String> codeStack;

    public ProcessLine(String codeLine){
        this.codeStack = toStack(codeLine);
    }

    private Stack<String> toStack(String codeLine){
        Stack<String> stack = new Stack<>();
        Collections.addAll(stack, codeLine.split(" "));
        return stack;
    }

    public String getSegment(){
        if(codeStack.size() == 0) return null;
        return codeStack.pop();
    }

    public int size(){
        return codeStack.size();
    }
}
