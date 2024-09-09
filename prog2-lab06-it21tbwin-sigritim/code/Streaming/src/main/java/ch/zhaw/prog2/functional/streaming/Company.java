package ch.zhaw.prog2.functional.streaming;

import ch.zhaw.prog2.functional.streaming.finance.CurrencyAmount;
import ch.zhaw.prog2.functional.streaming.finance.Payment;
import ch.zhaw.prog2.functional.streaming.humanresource.Employee;
import ch.zhaw.prog2.functional.streaming.humanresource.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This classe models a Company with all its Employees.
 * There might be Employees not working for the Company (e.g. temporally)
 * ✅  This class should be worked on by students.
 */
public class Company {
    private final List<Employee> employeeList;

    public Company(List<Employee> employeeList) {
        Objects.requireNonNull(employeeList);
        this.employeeList = employeeList;
    }

    /**
     * This method is provided by lecturer - do not change
     * Getter for all employees.
     *
     * @return List of employees, never {@code null}
     */
    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employeeList);
    }

    /*
     * Aufgabe a1)
     */
    public List<String> getDistinctFirstnamesOfEmployees() {
        return getAllEmployees().stream().map(Person::getFirstName).distinct().toList();

    }

    /*
     * Aufgabe a2)
     */
    public String[] getDistinctLastnamesOfEmployees() {
        return  getAllEmployees().stream().map(Person::getLastName).distinct().toArray(String[]::new);
    }

    /*
     * Aufgabe b)
     * There might be Employees not working for the Company (e.g. temporally)
     */
    public List<Employee> getEmployeesWorkingForCompany() {
        return getAllEmployees().stream().filter(Employee::isWorkingForCompany).toList();
    }

    /*
     * Aufgabe c) - Test in Klasse CompanyTestStudent
     */
    public List<Employee> getEmployeesByPredicate(Predicate<Employee> filterPredicate) {
        return getAllEmployees().stream().filter(filterPredicate).toList();
    }

    /**
     * This method is provided by lecturer - do not change
     * Create List of payments for employees which are selected by the employeePredicate
     *
     * @param employeePredicate Predicate-Function that returns true for all Employee which
     *                          get a payment
     * @return list of Payments
     */
    public List<Payment> getPayments(Predicate<Employee> employeePredicate) {
        List<Payment> paymentList = new ArrayList<>();
        for(Employee employee: employeeList) {
            if (employeePredicate.test(employee)) {
                Payment payment = new Payment();
                CurrencyAmount salary = employee.getYearlySalary();
                int paymentsPerYear = employee.getPaymentsPerYear().getValue();
                salary = salary.createModifiedAmount(amount -> amount / paymentsPerYear);
                payment.setCurrencyAmount(salary).setBeneficiary(employee).setTargetAccount(employee.getAccount());
                paymentList.add(payment);
            }
        }
        return paymentList;
    }



     /**
      * Aufgabe g1)
      *
      * This Method calculates a List of Payments using a (delegate) Function.
      * @param employeePredicate - predicate for Employees eligible for a Payements
      * @param paymentForEmployee - (delegate) Function Calculating a  Payment for an Employee
      * @return a List of Payments based on predicate and payment function
      */
    public List<Payment> getPayments(Predicate<Employee> employeePredicate, Function<Employee, Payment> paymentForEmployee) {
        return employeeList.stream().filter(employeePredicate).map(paymentForEmployee::apply).toList();
    }

    /**
     * Aufgabe g2)
     *
     * Function calculating Payment for January.
     */
    public static final Function<Employee, Payment> paymentForEmployeeJanuary = employee -> {
        return new Payment().setCurrencyAmount(new CurrencyAmount(employee.getYearlySalary().getAmount()/13, employee.getYearlySalary().getCurrency()));
    };

    /*
     * Aufgabe g3)
     *
     * Fuction calculating Payment for December, where Employees having 13 Payments per year will get the double amount.
     */
    public static final Function<Employee, Payment> paymentForEmployeeDecember = employee -> {
        return new Payment().setCurrencyAmount(new CurrencyAmount((employee.getYearlySalary().getAmount()/13)*2, employee.getYearlySalary().getCurrency()));
    };
}
