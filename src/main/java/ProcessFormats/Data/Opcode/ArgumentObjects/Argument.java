package ProcessFormats.Data.Opcode.ArgumentObjects;

public class Argument {
    private String stringArgument;
    private int intArgument;
    private AddressMode addressMode;

    public Argument(String stringArgument, AddressMode addressMode){
        formatStrArg(stringArgument);
        this.addressMode = addressMode;
    }

    public Argument(int intArgument, AddressMode addressMode){
        formatIntArg(intArgument);
        this.addressMode = addressMode;
    }

    public void setArgument(String arg){
        formatStrArg(arg);
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

    private void formatStrArg(String arg){
        this.stringArgument = arg;
        try{
            this.intArgument = Integer.parseInt(arg);
        }catch (Exception e){
            this.intArgument = 0;
        }
    }

    private void formatIntArg(int arg){
        this.stringArgument = Integer.toString(arg);
        this.intArgument = arg;
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
