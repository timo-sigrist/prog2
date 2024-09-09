package ch.zhaw.prog2.functional.streaming.humanresource;

import ch.zhaw.prog2.functional.streaming.finance.BankAccountSupplier;
import ch.zhaw.prog2.functional.streaming.finance.CurrencyAmountSupplier;
import ch.zhaw.prog2.functional.streaming.finance.PaymentsPerYear;

import java.util.Objects;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

public class EmployeeSupplier implements Supplier<Employee> {
    public static final short PERCENTAGE_OF_WORKING_EMPLOYEES = 90;
    public static final short PERCENTAGE_FEMALE = 50;
    private static final short PERCENTAGE_13_MONTH_PAYMENT = 50;
    private static final int SALARY_MAX = 100000;
    private static final String[] FIRSTNAMES = {
        "Lowri", "Molly", "Ria", "Irene", "Hazel", "Yasmin", "Alexia",
        "Kenneth", "Yasin", "Gerald", "Ciaran", "Rocco", "Glenn", "Bailey",
        "Evelyn", "Penelope", "Darcie", "Ellie-May", "Rhonda", "Lana", "Heather",
        "Raphael", "Oscar", "Liam", "Robert", "Declan", "Leroy", "Aiden"
    };
    private static final String[] LASTNAMES = {
        "Lamb", "Evans", "Rowe", "Ford", "Paul", "Turner", "Miller",
        "Peters", "Wang", "Davis", "Burton", "Faulkner", "Griffiths", "Owens",
        "O'Reilly", "Jacobs", "Sherman", "Howells", "Walters", "Warner", "Schroeder"
    };
    private final Random random;
    private final IntPredicate randomTrueForPercentage;
    private final CurrencyAmountSupplier currencyAmountSupplier;
    private final BankAccountSupplier accountSupplier;

    public EmployeeSupplier(Random random) {
        Objects.requireNonNull(random);
        this.random = new Random(random.nextLong());
        accountSupplier = new BankAccountSupplier(new Random(random.nextLong()));
        currencyAmountSupplier = new CurrencyAmountSupplier(new Random(random.nextLong()), SALARY_MAX);
        randomTrueForPercentage = percentTrue -> this.random.nextInt(100) + 1 <= percentTrue;
    }

    private String selectOne(String[] values) {
        int index = random.nextInt(values.length);
        return values[index];
    }

    @Override
    public Employee get() {
        PaymentsPerYear paymentsPerYear = randomTrueForPercentage.test(PERCENTAGE_13_MONTH_PAYMENT) ?
            PaymentsPerYear.THIRTEEN : PaymentsPerYear.TWELVE;

        Employee newEmployee = new Employee(selectOne(FIRSTNAMES), selectOne(LASTNAMES))
            .setWorkingForCompany(randomTrueForPercentage.test(PERCENTAGE_OF_WORKING_EMPLOYEES))
            .setPaymentsPerYear(paymentsPerYear)
            .setYearlySalary(currencyAmountSupplier.get())
            .setAccount(accountSupplier.get());
        newEmployee.setFemale(randomTrueForPercentage.test(PERCENTAGE_FEMALE));
        return newEmployee;
    }

}
