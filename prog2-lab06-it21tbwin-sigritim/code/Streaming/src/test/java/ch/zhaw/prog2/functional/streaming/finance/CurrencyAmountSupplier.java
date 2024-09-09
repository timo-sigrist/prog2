package ch.zhaw.prog2.functional.streaming.finance;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

public class CurrencyAmountSupplier implements Supplier<CurrencyAmount> {
    private final Random random;
    private final int maxAmount;
    private final CurrencySupplier currencySupplier;

    public CurrencyAmountSupplier(Random random, int maxAmount) {
        Objects.requireNonNull(random);
        this.random = random;
        this.maxAmount = maxAmount;
        this.currencySupplier = new CurrencySupplier(random);
    }

    @Override
    public CurrencyAmount get() {
        return new CurrencyAmount(random.nextInt(maxAmount), currencySupplier.get());
    }
}
