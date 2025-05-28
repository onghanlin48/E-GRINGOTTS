package data;

public class pin {
    private static String old;
    private static String new_pin;


    public pin() {

    }
    public static String getOld() {
        return old;
    }
    public void setOld(String old) {
        this.old = old;
    }
    public static String getNew_pin() {
        return new_pin;
    }
    public void setNew_pin(String new_pin) {
        this.new_pin = new_pin;
    }
    public void clear(){
        old = null;
        new_pin = null;
    }
}
