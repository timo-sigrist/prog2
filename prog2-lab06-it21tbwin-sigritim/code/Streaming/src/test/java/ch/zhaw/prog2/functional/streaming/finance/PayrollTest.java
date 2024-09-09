package ch.zhaw.prog2.functional.streaming.finance;

import ch.zhaw.prog2.functional.streaming.humanresource.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PayrollTest {
    private static final Person adam = new Person("Adam", "First");

    @Test
    void addPayments() {
        Payment firstPayment = new Payment().setBeneficiary(adam);
        Payroll payroll = new Payroll();
        List<Payment> paymentList = new ArrayList<>(1);
        paymentList.add(firstPayment);
        payroll.addPayments(paymentList);
        assertIterableEquals(paymentList, payroll.getPaymentList());

        assertThrows(IllegalArgumentException.class, () ->
            payroll.addPayments(paymentList), "detect duplicate beneficiary");
    }
}
