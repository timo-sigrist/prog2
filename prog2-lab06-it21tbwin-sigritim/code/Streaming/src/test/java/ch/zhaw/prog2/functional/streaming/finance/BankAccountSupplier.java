package ch.zhaw.prog2.functional.streaming.finance;

import java.util.Currency;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class BankAccountSupplier implements Supplier<BankAccount> {
    private final Random random;
    private final CurrencySupplier currencySupplier;

    public BankAccountSupplier(Random random) {
        Objects.requireNonNull(random);
        this.random = new Random(random.nextLong());
        currencySupplier = new CurrencySupplier(random);
    }

    @Override
    public BankAccount get() {
        BankAccount bankAccount = new BankAccount();
        Currency currency = currencySupplier.get();
        try {
            StringBuilder iban = new StringBuilder(currency.getCurrencyCode().substring(0, 2));
            iban.append(random.nextInt(90) + 10).append(" ");
            iban.append(random.nextInt(1_000_000_000)).append(" ");
            iban.append(random.nextInt(1_000_000_000));
            bankAccount.setCurrency(currency).setIbanNumber(iban.toString());
        } catch (IllegalIbanNumber illegalIbanNumber) {
            illegalIbanNumber.printStackTrace();
        }
        return bankAccount;
    }
}
