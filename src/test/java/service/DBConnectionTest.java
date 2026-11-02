package service;

import exception.PayrollServiceException;
import org.junit.jupiter.api.Test;
import utility.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DBConnectionTest {

    @Test
    void testConnectionEstablished() throws PayrollServiceException, SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            assertTrue(connection.isValid(2));
            assertEquals("payroll_service", connection.getCatalog());
        }
    }

    @Test
    void testMySqlDriverIsAvailableAndRegistered() throws PayrollServiceException {
        assertTrue(DBConnection.isDriverAvailable());
        DBConnection.loadDriver();
        assertTrue(DBConnection.getRegisteredDrivers().contains("com.mysql.cj.jdbc.Driver"));
    }

    @Test
    void testConnectionFailureIsPropagatedAsPayrollServiceException() {
        PayrollServiceException exception = assertThrows(PayrollServiceException.class,
                () -> DBConnection.getConnection(
                        "jdbc:mysql://localhost:3306/payroll_service",
                        "root",
                        "definitely-not-the-password"));

        assertNotNull(exception.getMessage());
        assertInstanceOf(SQLException.class, exception.getCause());
    }
}
