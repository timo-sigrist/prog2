package ch.zhaw.prog2.functional.streaming.finance;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankAccountSupplierTest {
    private static final long RANDOM_SEED = 119L;

    @Test
    void get() {
        Random random = new Random(RANDOM_SEED);
        BankAccountSupplier bankAccountSupplier = new BankAccountSupplier(random);
        int sampleSize = 10;
        long distinct = Stream.generate(bankAccountSupplier).limit(sampleSize).limit(sampleSize)
            .map(account -> account.getIbanNumber()).distinct().count();
        assertEquals(sampleSize, distinct, "all generated iban number have to differ");
    }
}
