package ch.zhaw.prog2.functional.streaming.humanresource;

import ch.zhaw.prog2.functional.streaming.finance.BankAccount;
import ch.zhaw.prog2.functional.streaming.finance.CurrencyAmount;
import ch.zhaw.prog2.functional.streaming.finance.PaymentsPerYear;

import java.util.Optional;

public class Employee extends Person {
    private CurrencyAmount yearlySalary;
    private PaymentsPerYear paymentsPerYear = PaymentsPerYear.THIRTEEN;
    private BankAccount account;
    private boolean isWorkingForCompany;

    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    /**
     * There might be Employees not working for the Company (e.g. temporally)
     */
    public boolean isWorkingForCompany() {
        return isWorkingForCompany;
    }

    /**
     * There might be Employees not working for the Company (e.g. temporally)
     * @param workingForCompany true if working 
     * @return
     */
    public Employee setWorkingForCompany(boolean workingForCompany) {
        isWorkingForCompany = workingForCompany;
        return this;
    }

    public CurrencyAmount getYearlySalary() {
        return yearlySalary;
    }

    public Employee setYearlySalary(CurrencyAmount yearlySalary) {
        this.yearlySalary = yearlySalary;
        return this;
    }

    public PaymentsPerYear getPaymentsPerYear() {
        return paymentsPerYear;
    }

    public Employee setPaymentsPerYear(PaymentsPerYear paymentsPerYear) {
        this.paymentsPerYear = paymentsPerYear;
        return this;
    }

    public Optional<BankAccount> getAccount() {
        return Optional.ofNullable(account);
    }

    public Employee setAccount(BankAccount account) {
        this.account = account;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("yearlySalaryCHF=").append(yearlySalary);
        sb.append(", paymentsPerYear=").append(paymentsPerYear);
        sb.append(", account=").append(account);
        sb.append(", person=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
