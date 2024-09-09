package ch.zhaw.prog2.functional.streaming.finance;

import java.util.Currency;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class CurrencySupplier implements Supplier<Currency> {
    // Java 14 knows 228 Currencies - we only want some of them
    private static final Set<String> CURRENCY_ISOCODES = Set.of("CHF", "EUR", "GBP", "USD");
    private static final Currency[] CURRENCIES = Currency.getAvailableCurrencies().stream()
        .filter(currency -> CurrencySupplier.CURRENCY_ISOCODES.contains(currency.getCurrencyCode()))
        .toArray(Currency[]::new);
    private final Random random;

    public CurrencySupplier(Random random) {
        this.random = random;
    }

    @Override
    public Currency get() {
        return CURRENCIES[random.nextInt(CURRENCIES.length)];
    }
}
