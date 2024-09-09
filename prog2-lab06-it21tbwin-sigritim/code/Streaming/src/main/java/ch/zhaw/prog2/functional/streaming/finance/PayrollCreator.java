package ch.zhaw.prog2.functional.streaming.finance;

import ch.zhaw.prog2.functional.streaming.Company;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Class creates a Payroll (Lohabrechnung) for a whole Company
 * and supplies some Utility Methods for a a Payroll.
 * ✅  This class should be worked on by students.
 */
public class PayrollCreator {
    private final Company company;

    /**
     * Opens a Payroll for a company.
     * @param company
     */
    public PayrollCreator(Company company) {
        this.company = company;
    }

    /*
     * Aufgabe d) - Test dazu exisitert in PayrollCreatorTest
     */
    public Payroll getPayrollForAll() {
        Payroll payrollForAll = new Payroll();
        payrollForAll.addPayments(company.getPayments(e -> e.isWorkingForCompany()));
        return payrollForAll;
    }

    /*
     * Aufgabe e) - Test dazu existiert in PayrollCreatorTest
     */
    public static int payrollValueCHF(Payroll payroll) {
        return payroll.stream().mapToInt(i -> CurrencyChange.getInNewCurrency(i.getCurrencyAmount(), Currency.getInstance("CHF")).getAmount()).sum();
    }

    /**
     * Aufgabe f) - schreiben Sie einen eigenen Test in PayrollCreatorTestStudent
     * @return a List of total amounts in this currency for each currency in the payroll
     */
    public static List<CurrencyAmount> payrollAmountByCurrency(Payroll payroll) {
        List<CurrencyAmount> currencies = new ArrayList<>();
        payroll.stream().collect(Collectors.groupingBy(p -> p.getCurrencyAmount().getCurrency(), Collectors.summingInt(p -> p.getCurrencyAmount().getAmount())))
            .forEach((cur, tot) -> currencies.add(new CurrencyAmount(tot, cur)));
        return currencies;
    }


}
