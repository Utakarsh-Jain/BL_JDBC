package utility;

import exception.PayrollServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public final class DBConnection {

    private static final String CONFIG_FILE = "application.properties";
    private static final String MYSQL_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private DBConnection() {
    }

    public static Connection getConnection() throws PayrollServiceException {
        Properties configuration = loadConfiguration();
        String url = resolveSetting("PAYROLL_DB_URL", configuration, "payroll.db.url", DEFAULT_URL);
        String user = resolveSetting("PAYROLL_DB_USER", configuration, "payroll.db.user", DEFAULT_USER);
        String password = resolveSetting("PAYROLL_DB_PASSWORD", configuration, "payroll.db.password", DEFAULT_PASSWORD);
        return getConnection(url, user, password);
    }

    public static Connection getConnection(String url, String user, String password) throws PayrollServiceException {
        loadDriver();
        printDriverInformation();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection == null || connection.isClosed()) {
                throw new PayrollServiceException("Unable to establish a connection to " + url);
            }
            System.out.println("Connection Established");
            return connection;
        } catch (SQLException e) {
            throw new PayrollServiceException("Unable to establish a connection to " + url, e);
        }
    }

    public static void loadDriver() throws PayrollServiceException {
        try {
            Class.forName(MYSQL_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new PayrollServiceException("MySQL JDBC driver class is not available: " + MYSQL_DRIVER_CLASS, e);
        }
    }

    public static boolean isDriverAvailable() {
        try {
            Class.forName(MYSQL_DRIVER_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static List<String> getRegisteredDrivers() {
        List<String> drivers = new ArrayList<>();
        Enumeration<Driver> registeredDrivers = DriverManager.getDrivers();
        while (registeredDrivers.hasMoreElements()) {
            drivers.add(registeredDrivers.nextElement().getClass().getName());
        }
        Collections.sort(drivers);
        return drivers;
    }

    private static void printDriverInformation() {
        System.out.println("MySQL JDBC driver class available: " + isDriverAvailable());
        System.out.println("Registered JDBC drivers:");
        for (String driver : getRegisteredDrivers()) {
            System.out.println(" - " + driver);
        }
    }

    private static String resolveSetting(String environmentVariable, Properties configuration,
                                         String propertyKey, String defaultValue) {
        String environmentValue = System.getenv(environmentVariable);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }
        return configuration.getProperty(propertyKey, defaultValue);
    }

    private static Properties loadConfiguration() throws PayrollServiceException {
        Properties configuration = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                configuration.load(inputStream);
            }
        } catch (IOException e) {
            throw new PayrollServiceException("Unable to read database configuration file: " + CONFIG_FILE, e);
        }
        return configuration;
    }
}
