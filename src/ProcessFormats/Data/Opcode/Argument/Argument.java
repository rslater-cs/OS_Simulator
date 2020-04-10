package ProcessFormats.Data.Opcode.Argument;

public class Argument {
    private String stringArgument;
    private int intArgument;
    private AddressMode addressMode;

    public Argument(String stringArgument, AddressMode addressMode){
        this.stringArgument = stringArgument;
        try {
            this.intArgument = Integer.parseInt(stringArgument);
        } catch(Exception e){
            this.intArgument = -1;
        }
        this.addressMode = addressMode;
    }

    public String getValue() {
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
        String result = getValue();
        if(addressMode == AddressMode.DIRECT){
            result = "#" + result;
        }
        return result;
    }
}
