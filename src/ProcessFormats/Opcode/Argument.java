package ProcessFormats.Opcode;

import FileHandler.Complier.Interpreter.Interpreter;

import java.lang.reflect.Array;

public class Argument {
    private String stringArgument;
    private int intArgument;
    private AddressMode addressMode;

    public Argument(String stringArgument, AddressMode addressMode){
        this.stringArgument = stringArgument;
        this.intArgument = Integer.parseInt(stringArgument);
        this.addressMode = addressMode;
    }

    public String getStringArgument() {
        return stringArgument;
    }

    public int getIntArgument() {
        return intArgument;
    }

    public AddressMode getAddressMode() {
        return addressMode;
    }

    @Override
    public String toString(){
        String result = getStringArgument();
        if(addressMode == AddressMode.DIRECT){
            result = "#" + result;
        }
        return result;
    }
}
