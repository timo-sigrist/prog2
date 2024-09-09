package ch.zhaw.prog2.functional.streaming.finance;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrencySupplierTest {
    private static final long RANDOM_SEED = 1173L;

    @Test
    void get() {
        Random random = new Random(RANDOM_SEED);
        CurrencySupplier currencySupplier = new CurrencySupplier(random);
        int sampleSize = 10;
        long distinct = Stream.generate(currencySupplier).limit(sampleSize).distinct().count();
        assertTrue(distinct > 2, "At least two different currencies expected");
    }
}
