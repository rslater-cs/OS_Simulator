package Shell.Text.Validater;

public class Functions {
    private static final String[] functions = {"run", "exit", "slowspeed"};

    public static boolean isFunction(String word){

        for(String function : functions){
            if(function.equals(word)){
                return true;
            }
        }
        return false;
    }
}
