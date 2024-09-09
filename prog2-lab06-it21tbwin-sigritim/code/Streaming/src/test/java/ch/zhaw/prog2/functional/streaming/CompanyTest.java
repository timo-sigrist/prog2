package ch.zhaw.prog2.functional.streaming;

import ch.zhaw.prog2.functional.streaming.finance.Payment;
import ch.zhaw.prog2.functional.streaming.humanresource.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class CompanyTest {
    // These variables are not private because some tests are in the class CompanyTestStudent.
    static final long RANDOM_SEED = 113L;
    static final int EMPLOYEE_COUNT = 500;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        Random random = new Random(RANDOM_SEED);
        CompanySupplier companySupplier = new CompanySupplier(random, EMPLOYEE_COUNT);
        testCompany = companySupplier.get();
    }

    @Test
    void constructor() {
        assertThrows(NullPointerException.class,
            () -> new Company(null), "null in constuctor not allowed");
        List<Employee> employeeList = testCompany.getAllEmployees();
        assertNotNull(employeeList);
        assertEquals(EMPLOYEE_COUNT, testCompany.getAllEmployees().size());
    }

    @Test
    void getAllEmployees() {
        assertEquals(EMPLOYEE_COUNT, testCompany.getAllEmployees().size(), "testCompany has given count of employees");
    }

    @Test
    void getPayments() {
        List<Payment> paymentList = testCompany.getPayments(employee -> false);
        assertNotNull(paymentList, "You have to implement the tested method");
        assertEquals(0, paymentList.size(), "No payments if Predicate evaluates always true");
    }

    @Test
    void getDistinctFirstnamesOfEmployees() {
        List<String> names = testCompany.getDistinctFirstnamesOfEmployees();
        assertNotNull(names, "You have to implement the tested method");
        assertEquals(28, names.size(), "default company has given number of distinct first names");
    }

    @Test
    void getDistinctLastnamesOfEmployees() {
        String[] names = testCompany.getDistinctLastnamesOfEmployees();
        assertNotNull(names, "You have to implement the tested method");
        assertEquals(21, names.length, "default company has given number of distinct last names");
    }

    @Test
    void getEmployeesWorkingForCompany() {
        List<Employee> workingEmployees = testCompany.getEmployeesWorkingForCompany();
        assertNotNull(workingEmployees, "You have to implement the tested method");
        assertTrue(workingEmployees.size() >= 400, "default company has at least 400 working employees");
    }

    @Test
    void testGetPayments() {
        Payment dummyPayment = mock(Payment.class);
        List<Payment> paymentList = testCompany.getPayments(employee -> false, employee -> dummyPayment);
        assertNotNull(paymentList, "You have to implement the tested method");
        assertEquals(List.of(), paymentList, "no employees");

        Predicate<Employee> allEmployee = employee -> true;
        paymentList = testCompany.getPayments(allEmployee, employee -> dummyPayment);
        assertEquals(EMPLOYEE_COUNT, paymentList.size(), "every employee gets payment");
        assertTrue(paymentList.stream().allMatch(payment -> payment == dummyPayment), "all payments are dummy payments");

        long januaryAmountSum = getAmountSum(testCompany.getPayments(allEmployee, Company.paymentForEmployeeJanuary));
        long decemberAmountSum = getAmountSum(testCompany.getPayments(allEmployee, Company.paymentForEmployeeDecember));
        long sumByMonth = 11 * januaryAmountSum + decemberAmountSum;

        Function<Employee, Payment> yearlySalary = employee -> new Payment()
            .setCurrencyAmount(employee.getYearlySalary())
            .setBeneficiary(employee);

        paymentList = testCompany.getPayments(employee -> true, yearlySalary);
        long yearlyAmountSum = CompanyTest.this.getAmountSum(paymentList);
        assertEquals(sumByMonth, yearlyAmountSum, EMPLOYEE_COUNT * 12, "sum of monthly payments have to match yearly sum");
    }

    private long getAmountSum(List<Payment> paymentList) {
        return paymentList.stream()
            .mapToInt(payment -> payment.getCurrencyAmount().getAmount())
            .sum();
    }
}
