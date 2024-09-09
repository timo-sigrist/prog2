package ch.zhaw.prog2.functional.streaming.finance;

import java.util.Currency;
import java.util.StringJoiner;
import java.util.function.IntUnaryOperator;

/**
 * Bind currency to the amount
 */
public class CurrencyAmount {
    public static final Currency CHF = Currency.getInstance("CHF");
    private final int amount;
    private final Currency currency;

    public CurrencyAmount(int amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Use {@link #CHF} as currency
     *
     * @param amount
     */
    public CurrencyAmount(int amount) {
        this(amount, CHF);
    }

    public int getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    
    /**
     * Creates a new CurrencyAmount based on this object, modified by the modifying function. 
     * @param modifyFunction - function to modify this amount. 
     * @return the new modified CurrencyAmount. 
     */
    public CurrencyAmount createModifiedAmount(IntUnaryOperator modifyFunction) {
        int newAmount = modifyFunction.applyAsInt(this.amount);
        return new CurrencyAmount(newAmount, this.currency);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrencyAmount.class.getSimpleName() + "[", "]")
            .add("amount=" + amount)
            .add("currency=" + currency)
            .toString();
    }
}
