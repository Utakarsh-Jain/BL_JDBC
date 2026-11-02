package service;

import exception.PayrollServiceException;
import model.EmployeePayrollData;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollDBService {

    private static final String SELECT_ALL_EMPLOYEE_PAYROLL = """
            SELECT e.employee_id, e.name, e.gender, e.phone, e.address, e.start_date,
                   p.basic_pay, p.deductions, p.taxable_pay, p.income_tax, p.net_pay,
                   GROUP_CONCAT(d.department_name ORDER BY d.department_name SEPARATOR ', ') AS department
            FROM employee e
            JOIN payroll p ON p.employee_id = e.employee_id
            LEFT JOIN employee_department ed ON ed.employee_id = e.employee_id
            LEFT JOIN department d ON d.department_id = ed.department_id
            GROUP BY e.employee_id, e.name, e.gender, e.phone, e.address, e.start_date,
                     p.basic_pay, p.deductions, p.taxable_pay, p.income_tax, p.net_pay
            ORDER BY e.employee_id
            """;

    public List<EmployeePayrollData> getEmployeePayrollData() throws PayrollServiceException {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_EMPLOYEE_PAYROLL)) {
            while (resultSet.next()) {
                employeePayrollDataList.add(mapResultSetToEmployeePayrollData(resultSet));
            }
            return employeePayrollDataList;
        } catch (SQLException e) {
            throw new PayrollServiceException("Unable to retrieve employee payroll data", e);
        }
    }

    private EmployeePayrollData mapResultSetToEmployeePayrollData(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("employee_id");
        String name = resultSet.getString("name");
        double salary = resultSet.getDouble("basic_pay");
        LocalDate startDate = resultSet.getDate("start_date").toLocalDate();

        EmployeePayrollData employeePayrollData = new EmployeePayrollData(id, name, salary, startDate);
        employeePayrollData.setGender(resultSet.getString("gender").charAt(0));
        employeePayrollData.setPhone(resultSet.getString("phone"));
        employeePayrollData.setAddress(resultSet.getString("address"));
        employeePayrollData.setDepartment(resultSet.getString("department"));
        employeePayrollData.setDeductions(resultSet.getDouble("deductions"));
        employeePayrollData.setTaxablePay(resultSet.getDouble("taxable_pay"));
        employeePayrollData.setIncomeTax(resultSet.getDouble("income_tax"));
        employeePayrollData.setNetPay(resultSet.getDouble("net_pay"));
        return employeePayrollData;
    }
}
