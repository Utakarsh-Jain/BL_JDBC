package model;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {

    private int id;
    private String name;
    private double salary;
    private LocalDate startDate;
    private char gender;
    private String phone;
    private String address;
    private String department;
    private double deductions;
    private double taxablePay;
    private double incomeTax;
    private double netPay;

    public EmployeePayrollData(int id, String name, double salary) {
        this(id, name, salary, null);
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getBasicPay() {
        return salary;
    }

    public void setBasicPay(double basicPay) {
        this.salary = basicPay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getTaxablePay() {
        return taxablePay;
    }

    public void setTaxablePay(double taxablePay) {
        this.taxablePay = taxablePay;
    }

    public double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(double incomeTax) {
        this.incomeTax = incomeTax;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EmployeePayrollData other)) {
            return false;
        }
        return id == other.id
                && Double.compare(salary, other.salary) == 0
                && startDate == other.startDate
                && gender == other.gender
                && Double.compare(deductions, other.deductions) == 0
                && Double.compare(taxablePay, other.taxablePay) == 0
                && Double.compare(incomeTax, other.incomeTax) == 0
                && Double.compare(netPay, other.netPay) == 0
                && Objects.equals(name, other.name)
                && Objects.equals(phone, other.phone)
                && Objects.equals(address, other.address)
                && Objects.equals(department, other.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, startDate, gender, phone, address, department,
                deductions, taxablePay, incomeTax, netPay);
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", salary=" + salary
                + ", startDate=" + startDate
                + ", gender=" + gender
                + ", phone='" + phone + '\''
                + ", address='" + address + '\''
                + ", department='" + department + '\''
                + ", deductions=" + deductions
                + ", taxablePay=" + taxablePay
                + ", incomeTax=" + incomeTax
                + ", netPay=" + netPay
                + '}';
    }
}
