package ch.zhaw.prog2.functional.streaming.finance;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class is for all test methods written by students for easier review by lecturers.
 * In a real application these test would be in the class PayrollCreatorTest.
 *
 * ✅  This class should be worked on by students.
 */
public class PayrollCreatorTestStudent {
    static final int AMOUNT_CHF = 500;
    static final int AMOUNT_EUR = 1000;
    static final int AMOUNT_USD = 1200;
    static final String CHF = "CHF";
    static final String EUR = "EUR";
    static final String USD = "USD";


    @Test
    public void payrollAmountByCurrencyTest() {
        Payroll mockedPayroll = Mockito.mock(Payroll.class);
        // 3 mal CHF , 2 mal euro und 1 mal USD
        List<Payment> payments = new ArrayList<>();
        payments.add((new Payment()).setCurrencyAmount(new CurrencyAmount(AMOUNT_CHF, Currency.getInstance(CHF))));
        payments.add((new Payment()).setCurrencyAmount(new CurrencyAmount(AMOUNT_EUR, Currency.getInstance(EUR))));
        payments.add((new Payment()).setCurrencyAmount(new CurrencyAmount(AMOUNT_USD, Currency.getInstance(USD))));
        payments.add((new Payment()).setCurrencyAmount(new CurrencyAmount(AMOUNT_CHF, Currency.getInstance(CHF))));
        payments.add((new Payment()).setCurrencyAmount(new CurrencyAmount(AMOUNT_EUR, Currency.getInstance(EUR))));
        payments.add((new Payment()).setCurrencyAmount(new CurrencyAmount(AMOUNT_CHF, Currency.getInstance(CHF))));
        Mockito.when(mockedPayroll.stream()).thenReturn(payments.stream());

        List<CurrencyAmount> payRollAmountByCurrency = PayrollCreator.payrollAmountByCurrency(mockedPayroll);

        int allCHF = payRollAmountByCurrency.stream().filter(currencyAmount -> currencyAmount.getCurrency().toString().equals(CHF)).mapToInt(CurrencyAmount::getAmount).sum();
        int allEuro = payRollAmountByCurrency.stream().filter(currencyAmount -> currencyAmount.getCurrency().toString().equals(EUR)).mapToInt(CurrencyAmount::getAmount).sum();
        int allDollar = payRollAmountByCurrency.stream().filter(currencyAmount -> currencyAmount.getCurrency().toString().equals(USD)).mapToInt(CurrencyAmount::getAmount).sum();

        assertEquals(3*AMOUNT_CHF, allCHF);
        assertEquals(2*AMOUNT_EUR, allEuro);
        assertEquals(AMOUNT_USD, allDollar);
    }

}
