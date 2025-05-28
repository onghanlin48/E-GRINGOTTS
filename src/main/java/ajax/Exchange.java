package ajax;
import java.util.ArrayList;
import java.util.Stack;

public class Exchange{
    public static void main(String[] args) {
        String x = String.valueOf(2);
        Conversion<String,Double> booth=new Conversion<>();
        booth.addCurrency("Knut");
        booth.addCurrency("Sickle");
        booth.addCurrency("Galleon");
        booth.addToCurrency("Knut","Sickle",29.0,0.01);
        booth.addToCurrency("Knut","Galleon",493.0,0.2);
        booth.addToCurrency("Sickle","Knut",0.03448,0.03);
        booth.addToCurrency("Sickle","Galleon",17.0,0.08);
        booth.addToCurrency("Galleon","Knut",0.002028,0.05);
        booth.addToCurrency("Galleon","Sickle",0.05882,0.12);
        double converting = booth.DFSrate("Sickle", "Galleon", 100.0);
        System.out.printf("Converted amount: %.2f\n",converting);
        double processing= booth.DFSprocessingfee("Sickle", "Galleon", 100.0);
        System.out.printf("Total processing fees: %.2f\n",processing);
    }
}

class Currency<T extends Comparable<T>,N extends Comparable<N>>{
    T infocurrency;
    Currency<T,N> nextCurrency;
    ToCurrency<T,N> ToFirst;

    public Currency(){
        infocurrency=null;
        nextCurrency=null;
        ToFirst=null;
    }

    public Currency(T infocur,Currency<T,N> nextCur){
        infocurrency=infocur;
        nextCurrency=nextCur;
        ToFirst=null;
    }
}

class ToCurrency<T extends Comparable<T>,N extends Comparable<N>>{
    Currency<T,N> nextCurrency;
    N rate;
    N processingfee;
    ToCurrency<T,N> TonextCurrency;

    public ToCurrency(){
        nextCurrency=null;
        rate=null;
        processingfee=null;
        TonextCurrency=null;
    }

    public ToCurrency(Currency<T,N> currencytochange,N rates,N processingfees,ToCurrency<T,N> ToNext){
        nextCurrency=currencytochange;
        rate=rates;
        processingfee=processingfees;
        TonextCurrency=ToNext;
    }
}

class Conversion<T extends Comparable<T>,N extends Number & Comparable<N>> {
    Currency<T, N> firstCurrency;
    ArrayList<Currency> visited = new ArrayList<>();
    Stack<Currency> dfs = new Stack<>();
    Stack<Double> dfsrate = new Stack<>();
    Stack<Double> dfsprocessingfees = new Stack<>();


    public void clear() {
        firstCurrency = null;
    }

    public boolean CurrencyExists(T info) {
        if (firstCurrency == null)
            return false;
        Currency<T, N> temp = firstCurrency;
        while (temp != null) {
            if (temp.infocurrency.compareTo(info) == 0)
                return true;
            temp = temp.nextCurrency;
        }
        return false;
    }

    public boolean addCurrency(T currency) {
        if (CurrencyExists(currency) == false) {
            Currency<T, N> temp = firstCurrency;
            Currency<T, N> newCurrency = new Currency<>(currency, null);
            if (firstCurrency == null)
                firstCurrency = newCurrency;
            else {
                Currency<T, N> prev = firstCurrency;
                while (temp != null) {
                    prev = temp;
                    temp = temp.nextCurrency;
                }
                prev.nextCurrency = newCurrency;
            }
            return true;
        } else
            return false;
    }

    public boolean addToCurrency(T Currency1, T Currency2, N rate, N processingfee) {
        if (firstCurrency == null)
            return false;
        if (!CurrencyExists(Currency1) || !CurrencyExists(Currency2))
            return false;
        Currency<T, N> source = firstCurrency;
        while (source != null) {
            if (source.infocurrency.compareTo(Currency1) == 0) {
                Currency<T, N> destination = firstCurrency;
                while (destination != null) {
                    if (destination.infocurrency.compareTo(Currency2) == 0) {
                        ToCurrency<T, N> current = source.ToFirst;
                        ToCurrency<T, N> newToCurrency = new ToCurrency<>(destination, rate, processingfee, current);
                        source.ToFirst = newToCurrency;
                        return true;
                    }
                    destination = destination.nextCurrency;
                }
            }
            source = source.nextCurrency;
        }
        return false;
    }

    public boolean ToCurrencyExists(T Currency1, T Currency2) {
        if (firstCurrency == null)
            return false;
        if (!CurrencyExists(Currency1) || !CurrencyExists(Currency2))
            return false;
        Currency<T, N> source = firstCurrency;
        while (source != null) {
            if (source.infocurrency.compareTo(Currency1) == 0) {
                ToCurrency<T, N> current = source.ToFirst;
                while (current != null) {
                    if (current.nextCurrency.infocurrency.compareTo(Currency2) == 0)
                        return true;
                    current = current.TonextCurrency;
                }
            }
            source = source.nextCurrency;
        }
        return false;
    }

    public Currency<T, N> getCurrency(T input) {
        if (!CurrencyExists(input))
            return null;
        Currency<T, N> temp = firstCurrency;
        while (temp != null) {
            if (temp.infocurrency.equals(input))
                return temp;
            temp = temp.nextCurrency;
        }
        return null;
    }

    public double DFSrate(T src, T destination, double amount) {
        if (!CurrencyExists(src) || !CurrencyExists(destination))
            return 0;
        visited.clear();
        dfs.clear();
        dfsrate.clear();
        Currency<T, N> srcCurrency = getCurrency(src);
        Currency<T, N> destCurrency = getCurrency(destination);
        dfs.push(srcCurrency);
        dfsrate.push(1.0);
        while (!dfs.isEmpty()) {
            Currency<T, N> getCurrent = dfs.pop();
            double currentRate = dfsrate.pop();
            if (getCurrent.infocurrency.equals(destination)) {
                return amount * currentRate;
            }
            visited.add(getCurrent);
            for (ToCurrency<T, N> temp = getCurrent.ToFirst; temp != null; temp = temp.TonextCurrency) {
                if (!visited.contains(temp.nextCurrency)) {
                    dfs.push(temp.nextCurrency);
                    dfsrate.push(currentRate * temp.rate.doubleValue());
                }
            }
        }
        return 0;
    }

    public double DFSprocessingfee(T src, T destination, double amount) {
        if (!CurrencyExists(src) || !CurrencyExists(destination))
            return 0;
        visited.clear();
        dfs.clear();
        dfsprocessingfees.clear();
        Currency<T, N> srcCurrency = getCurrency(src);
        Currency<T, N> destCurrency = getCurrency(destination);
        dfs.push(srcCurrency);
        dfsprocessingfees.push(0.0);
        double total= 0.0;
        while (!dfs.isEmpty()) {
            Currency<T, N> current = dfs.pop();
            double currentFee = dfsprocessingfees.pop();
            if (current.infocurrency.equals(destination)) {
                total+= currentFee;
                return total;
            }
            visited.add(current);
            for (ToCurrency<T, N> temp = current.ToFirst; temp != null; temp = temp.TonextCurrency) {
                if (!visited.contains(temp.nextCurrency)) {
                    dfs.push(temp.nextCurrency);
                    dfsprocessingfees.push(currentFee+(amount * temp.processingfee.doubleValue()));
                }
            }
        }
        return 0;
    }
}
