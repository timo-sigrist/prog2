package ch.zhaw.prog2.functional.streaming.finance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyAmountTest {
    public static final int TEST_AMOUNT = 4321;

    @Test
    void constructor() {
        CurrencyAmount currencyAmount = new CurrencyAmount(TEST_AMOUNT);
        assertEquals(CurrencyAmount.CHF, currencyAmount.getCurrency());
        assertEquals(TEST_AMOUNT, currencyAmount.getAmount());
    }

    @Test
    void createModifiedAmount() {
        CurrencyAmount currencyAmount = new CurrencyAmount(TEST_AMOUNT);
        int factor = 17;
        CurrencyAmount newAmount = currencyAmount.createModifiedAmount(x -> x * factor);
        assertEquals(TEST_AMOUNT * factor, newAmount.getAmount());
        assertEquals(currencyAmount.getCurrency(), newAmount.getCurrency());
    }
}
