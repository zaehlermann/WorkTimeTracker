package de.zaehlermann.timetracker.model;

public class Employee {

  private final String userId;
  private final String rfid;
  private final String firstName;
  private final String lastName;

  public Employee(final String rfid) {
    this.rfid = rfid;
    this.userId = "0815";
    this.firstName = "Max";
    this.lastName = "Mustermann";
  }

  @Override
  public String toString() {
    return "EmployeeID: " + userId + System.lineSeparator() +
           "RFID:" + rfid + System.lineSeparator() +
           "Name: " + firstName + " " + lastName;
  }
}
