package ajax;

public class check_amount {
    private double amount;
    private double fee;
    private int from;
    private double dis;
    private int to;
    private String value;
    private String s;
    private double t;
    private double converting;
    private String get;
    private String rate;


    public double getAmount(){
        return amount;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public double getDis() {
        return dis;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double getConverting() {
        return converting;
    }

    public void setConverting(double converting) {
        this.converting = converting;
    }
    public String getGet() {
        return get;
    }
    public void setGet(String get) {
        this.get = get;
    }
    public String getRate() {
        return rate;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
    public void clear(){
        amount = 0;
        fee = 0;
        from = 0;
        dis = 0;
        to = 0;
        value = null;
        s = null;
        t = 0;
        converting = 0;
        get = null;
        rate = null;
    }
}
