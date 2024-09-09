package ch.zhaw.prog2.functional.streaming.finance;

import ch.zhaw.prog2.functional.streaming.humanresource.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * A Payroll (Lohnabrechnung) is principally a List of Payments for the Company.  
 * In this Payroll only one Payment for each beneficiary is allowed
 */
public class Payroll {
    private final List<Payment> paymentList = new ArrayList<>();

    public List<Payment> getPaymentList() {
        return Collections.unmodifiableList(paymentList);
    }

    /**
     * This Method will add more Payments to this Payroll an throw an IllegalArgumentException 
     * if we already have a Payment beloging to the same Person in this Payroll. 
     * @param morePayments
     */
    public void addPayments(List<Payment> morePayments) {
        if (hasSameBeneficiaryInefficient(morePayments)) {
            throw new IllegalArgumentException("Duplicate Beneficiary detected");
        } else {
            paymentList.addAll(morePayments);
        }
    }

    // this method is inefficient and should be rewritten by staff (not students)
    private boolean hasSameBeneficiaryInefficient(List<Payment> paymentListToVerify) {
        boolean res = false;
        for (Payment payment : paymentListToVerify) {
            Person beneficiary = payment.getBeneficiary();
            for (Payment checkPayment : paymentList) {
                if (beneficiary.equals(checkPayment.getBeneficiary())) {
                    res = true;
                }
            }
        }
        return res;
    }

    public Stream<Payment> stream() {
        return paymentList.stream();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Payroll.class.getSimpleName() + "[", "]")
            .add("paymentList=" + paymentList)
            .toString();
    }
}
