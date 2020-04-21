package FileHandler.Complier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Variables {
    private ArrayList<String> variableNames = new ArrayList<>();
    private int relativeAddress = 0;
    private boolean isBuilt = false;
    private Map<String, String> variables = new HashMap<>();
    private static final String VARIABLE_REGEX = "[a-zA-Z][a-zA-Z0-9]*";

    private String add(String name){
        variables.put(name, Integer.toString(relativeAddress++));
        return variables.get(name);
    }

    public String replace(String name){
        if(variables.containsKey(name)) return variables.get(name);
        return add(name);
    }

    public int size(){
        return variables.size();
    }

    @Override
    public String toString() {
        StringBuffer variableSummary = new StringBuffer();

        for(String name : variables.keySet()){
            variableSummary.append(name);
            variableSummary.append(" : ");
            variableSummary.append(variables.get(name));
            variableSummary.append("\n");
        }

        return variableSummary.toString();
    }
}
