package ch.zhaw.prog2.functional.streaming.humanresource;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeSupplierTest {
    private static final long RANDOM_SEED = 42L;

    @Test
    void get() {
        Random random = new Random(RANDOM_SEED);
        EmployeeSupplier employeeSupplier = new EmployeeSupplier(random);
        Employee firstEmployee = employeeSupplier.get();
        Employee secondEmployee = employeeSupplier.get();
        assertNotEquals(firstEmployee.getName(), secondEmployee.getName(), "Generated Employees have to differ");
    }
}
