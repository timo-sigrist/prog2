package ch.zhaw.prog2.functional.streaming.humanresource;

import ch.zhaw.prog2.functional.streaming.finance.BankAccount;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    private static final String TEST_FIRSTNAME = "Valeria";
    private static final String TEST_LASTNAME = "Sherman";
    private final Employee defaultEmployee = new Employee(TEST_FIRSTNAME, TEST_LASTNAME);

    @Test
    void getYearlySalary() {
        assertNull(defaultEmployee.getYearlySalary());
    }

    @Test
    void getAccount() {
        Optional<BankAccount> account = defaultEmployee.getAccount();
        assertTrue(account.isEmpty());
    }

    @Test
    void setAccount() {
        BankAccount account = new BankAccount();
        Employee employee = new Employee(TEST_FIRSTNAME, TEST_LASTNAME);
        employee.setAccount(account);
        assertEquals(account, employee.getAccount().orElse(null));
    }
}
