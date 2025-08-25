package de.zaehlermann.timetracker.model;

import java.util.Objects;

import javax.annotation.Nonnull;

public class Employee implements AbstractCsvEntity<Employee> {

  public static final String HEADER_LINE = "EMPLOYEE_ID;RFID;FIRSTNAME;LASTNAME";

  private final String employeeId;
  private final String rfid;
  private final String firstName;
  private final String lastName;

  public Employee(@Nonnull final String employeeId,
                  @Nonnull final String rfid,
                  @Nonnull final String firstName,
                  @Nonnull final String lastName) {
    this.employeeId = employeeId;
    this.rfid = rfid;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Nonnull
  public String toJournalTxtHeader() {
    return "EmployeeID: " + employeeId + System.lineSeparator() +
           "RFID: " + rfid + System.lineSeparator() +
           "Name: " + firstName + " " + lastName;
  }

  @Nonnull
  public static Employee fromCsvLine(@Nonnull final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    return new Employee(split[0], split[1], split[2], split[3]);
  }

  @Nonnull
  @Override
  public String toCsvLine() {
    return employeeId + ";" + rfid + ";" + firstName + ";" + lastName + System.lineSeparator();
  }

  @Nonnull
  public String getFirstName() {
    return firstName;
  }

  @Nonnull
  public String getLastName() {
    return lastName;
  }

  @Nonnull
  public String getRfid() {
    return rfid;
  }

  @Nonnull
  public String getEmployeeId() {
    return employeeId;
  }

  @Override
  public boolean equals(final Object o) {
    if(!(o instanceof final Employee employee)) return false;
    return Objects.equals(rfid, employee.rfid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(rfid);
  }
}
