package de.zaehlermann.timetracker.model;

import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Employee implements AbstractCsvEntity<Employee> {

  public static final String HEADER_LINE = "EMPLOYEE_ID;RFID;FIRSTNAME;LASTNAME";

  private final String employeeId;
  private final String rfid;
  private final String firstName;
  private final String lastName;

  public Employee(@NonNull final String employeeId,
                  @NonNull final String rfid,
                  @NonNull final String firstName,
                  @NonNull final String lastName) {
    this.employeeId = employeeId;
    this.rfid = rfid;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @NonNull
  public List<JournalSummaryItem> getSummaryItems() {
    return List.of(
      new JournalSummaryItem(MessageKeys.EMPLOYEE_ID.getTranslation(), employeeId),
      new JournalSummaryItem(Messages.get(MessageKeys.EMPLOYEE_NAME), firstName + " " + lastName),
      new JournalSummaryItem(Messages.get(MessageKeys.RFID), rfid));
  }

  @NonNull
  public static Employee fromCsvLine(@NonNull final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    return new Employee(split[0], split[1], split[2], split[3]);
  }

  @NonNull
  @Override
  public String toCsvLine() {
    return employeeId + ";" + rfid + ";" + firstName + ";" + lastName + System.lineSeparator();
  }

  @NonNull
  public String getFirstName() {
    return firstName;
  }

  @NonNull
  public String getLastName() {
    return lastName;
  }

  @NonNull
  public String getRfid() {
    return rfid;
  }

  @NonNull
  public String getEmployeeId() {
    return employeeId;
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof final Employee employee)) return false;
    return Objects.equals(rfid, employee.rfid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(rfid);
  }
}
