package ch.zhaw.prog2.functional.streaming.finance;

import java.util.Currency;

/**
 * Information needed to transfer money: IBAN-Number and Currency
 */
public class BankAccount {
    public static final String RELAXED_IABN_REGEX = "[A-Z][A-Z][0-9][0-9][A-Z0-9]{1,30}";
    private Currency currency = Currency.getInstance("CHF");
    private String ibanNumber;

    /**
     * Check if {@link #setIbanNumber(String) setIbanNumber}  will accept the given ibanNumber.
     *
     * @param ibanNumber the IBAN Number to check
     * @return true, if ibanNumber will be accepted
     */
    public static boolean isIbanAccepted(String ibanNumber) {
        return removeSpaces(ibanNumber).matches(RELAXED_IABN_REGEX);
    }

    private static String removeSpaces(String in) {
        return in.replace(" ", "");
    }

    public Currency getCurrency() {
        return currency;
    }

    public BankAccount setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public String getIbanNumber() {
        return ibanNumber;
    }

    /**
     * Setter method
     *
     * @param ibanNumber must be acceptable, see {@link #isIbanAccepted(String)}
     * @return this
     * @throws IllegalIbanNumber if ibanNumber can not be accepted
     */
    public BankAccount setIbanNumber(String ibanNumber) throws IllegalIbanNumber {
        if (isIbanAccepted(ibanNumber)) {
            this.ibanNumber = ibanNumber;
        } else {
            throw new IllegalArgumentException("IBAN is not accepted");
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BankAccount{");
        sb.append("currency=").append(currency);
        sb.append(", ibanNumber='").append(ibanNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
