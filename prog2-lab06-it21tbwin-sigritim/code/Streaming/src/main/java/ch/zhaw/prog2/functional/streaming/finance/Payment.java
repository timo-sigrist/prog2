package ch.zhaw.prog2.functional.streaming.finance;

import ch.zhaw.prog2.functional.streaming.humanresource.Person;

import java.util.Optional;
import java.util.StringJoiner;

/**
 * All information needed to pay an amount to a BankAccount of a Person.
 */
public class Payment {
    private BankAccount targetAccount;
    private CurrencyAmount currencyAmount;
    private Person beneficiary;

    public BankAccount getTargetAccount() {
        return targetAccount;
    }

    public Payment setTargetAccount(BankAccount targetAccount) {
        this.targetAccount = targetAccount;
        return this;
    }

    public Payment setTargetAccount(Optional<BankAccount> targetAccount) {
        return setTargetAccount(targetAccount.orElse(null));
    }

    public CurrencyAmount getCurrencyAmount() {
        return currencyAmount;
    }

    public Payment setCurrencyAmount(CurrencyAmount currencyAmount) {
        this.currencyAmount = currencyAmount;
        return this;
    }

    public Person getBeneficiary() {
        return beneficiary;
    }

    public Payment setBeneficiary(Person beneficiary) {
        this.beneficiary = beneficiary;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Payment.class.getSimpleName() + "[", "]")
            .add("targetAccount=" + targetAccount)
            .add("currencyAmount=" + currencyAmount)
            .add("beneficiary=" + beneficiary)
            .toString();
    }
}
