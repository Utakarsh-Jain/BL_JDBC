package service;

import exception.PayrollServiceException;
import model.EmployeePayrollData;
import org.junit.jupiter.api.Test;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayrollDBServiceTest {

    private final PayrollDBService payrollDBService = new PayrollDBService();

    @Test
    void testRetrieveAllEmployeePayrollDataAgainstDatabase() throws Exception {
        List<EmployeePayrollData> employeePayrollData = payrollDBService.getEmployeePayrollData();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT COUNT(*) FROM employee e JOIN payroll p ON p.employee_id = e.employee_id")) {
            assertTrue(resultSet.next());
            assertEquals(resultSet.getInt(1), employeePayrollData.size());
        }

        Set<String> names = employeePayrollData.stream()
                .map(EmployeePayrollData::getName)
                .collect(Collectors.toSet());
        assertTrue(names.containsAll(Set.of("Bill", "Charlie", "Terisa")));
    }

    @Test
    void testEmployeePayrollObjectsContainCorrectDatabaseValues() throws Exception {
        List<EmployeePayrollData> employeePayrollData = payrollDBService.getEmployeePayrollData();

        String query = """
                SELECT e.employee_id, e.name, e.gender, e.phone, e.address, e.start_date,
                       p.basic_pay, p.deductions, p.taxable_pay, p.income_tax, p.net_pay
                FROM employee e
                JOIN payroll p ON p.employee_id = e.employee_id
                ORDER BY e.employee_id
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            int index = 0;
            while (resultSet.next()) {
                EmployeePayrollData employee = employeePayrollData.get(index++);
                assertEquals(resultSet.getInt("employee_id"), employee.getId());
                assertEquals(resultSet.getString("name"), employee.getName());
                assertEquals(resultSet.getDouble("basic_pay"), employee.getSalary(), 0.001);
                assertEquals(resultSet.getDouble("basic_pay"), employee.getBasicPay(), 0.001);
                assertEquals(resultSet.getDate("start_date").toLocalDate(), employee.getStartDate());
                assertEquals(resultSet.getString("gender").charAt(0), employee.getGender());
                assertEquals(resultSet.getString("phone"), employee.getPhone());
                assertEquals(resultSet.getString("address"), employee.getAddress());
                assertEquals(resultSet.getDouble("deductions"), employee.getDeductions(), 0.001);
                assertEquals(resultSet.getDouble("taxable_pay"), employee.getTaxablePay(), 0.001);
                assertEquals(resultSet.getDouble("income_tax"), employee.getIncomeTax(), 0.001);
                assertEquals(resultSet.getDouble("net_pay"), employee.getNetPay(), 0.001);
            }
            assertEquals(employeePayrollData.size(), index);
        }
    }

    @Test
    void testStartDateIsPopulatedForEveryEmployee() throws PayrollServiceException {
        List<EmployeePayrollData> employeePayrollData = payrollDBService.getEmployeePayrollData();

        assertFalse(employeePayrollData.isEmpty());
        assertTrue(employeePayrollData.stream().allMatch(employee -> employee.getStartDate() != null));

        EmployeePayrollData bill = employeePayrollData.stream()
                .filter(employee -> "Bill".equals(employee.getName()))
                .findFirst()
                .orElseThrow();
        assertNotNull(bill.getStartDate());
        assertEquals(LocalDate.of(2018, 1, 1), bill.getStartDate());
    }
}
