package FileHandler.Complier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Variables {
    private ArrayList<String> variableNames = new ArrayList<>();
    private int relativeAddress = 1;
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
}
