package FileHandler.Complier.Interpreter.Token;

import ProcessFormats.Opcode.Argument.AddressMode;

public class Token {
    private AddressMode addressMode;
    private String value;

    public Token(AddressMode addressMode, String value){
        this.addressMode = addressMode;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public AddressMode getAddressMode() {
        return addressMode;
    }

    @Override
    public String toString(){
        return addressMode + ", " + value;
    }
}
