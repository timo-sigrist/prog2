package ch.zhaw.prog2.functional.streaming.finance;

import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyChangeTest {
    private static final CurrencyAmount gbp113 = new CurrencyAmount(113, Currency.getInstance("GBP"));
    private static final Currency CURRENCY_CHF = Currency.getInstance("CHF");
    private static final Currency CURRENCY_USD = Currency.getInstance("USD");
    private static final Currency CURRENCY_GBP = Currency.getInstance("GBP");

    @Test
    void getInNewCurrency() {
        CurrencyAmount unchanged = CurrencyChange.getInNewCurrency(gbp113, CURRENCY_GBP);
        assertEquals(gbp113.getAmount(), unchanged.getAmount(), "no conversion keeps value");
        assertEquals(CURRENCY_GBP, unchanged.getCurrency(), "target currency is GBP");

        CurrencyAmount newCurrencyAmount = CurrencyChange.getInNewCurrency(gbp113, CURRENCY_CHF);
        // TODO fix magic numbers - CurrencyChange should get map with factors in constructor
        assertEquals(113 / 0.83, newCurrencyAmount.getAmount() * 1.0, 0.5);
        assertEquals("CHF", newCurrencyAmount.getCurrency().getCurrencyCode());

        newCurrencyAmount = CurrencyChange.getInNewCurrency(gbp113, CURRENCY_USD);
        // TODO fix magic numbers - CurrencyChange should get map with factors in constructor
        assertEquals(113 / 0.83 * 1.04, newCurrencyAmount.getAmount() * 1.0, 0.5);
        assertEquals("USD", newCurrencyAmount.getCurrency().getCurrencyCode());
    }
}
