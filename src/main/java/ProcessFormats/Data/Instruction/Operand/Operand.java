package ProcessFormats.Data.Instruction.Operand;

public class Operand {
    private String stringArgument;
    private int intArgument;
    private AddressMode addressMode;

    public Operand(String stringArgument, AddressMode addressMode){
        formatStrArg(stringArgument);
        this.addressMode = addressMode;
    }

    public Operand(int intArgument, AddressMode addressMode){
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
    public boolean equals(Object obj) {
        if(obj == null) return false;
        Operand operand = (Operand)obj;

        return (operand.addressMode == addressMode && operand.intArgument == intArgument);
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
