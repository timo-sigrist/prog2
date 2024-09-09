package ch.zhaw.prog2.functional.streaming.finance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentTest {
    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
    }

    @Test
    void setTargetAccount() {
        Optional<BankAccount> account = Optional.empty();
        payment.setTargetAccount(account);
        assertNull(payment.getTargetAccount());

        BankAccount realAccount = new BankAccount();
        account = Optional.of(realAccount);
        payment.setTargetAccount(account);
        assertEquals(realAccount, payment.getTargetAccount(), "get stored account value");
    }
}
