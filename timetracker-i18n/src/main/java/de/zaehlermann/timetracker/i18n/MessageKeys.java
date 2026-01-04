package de.zaehlermann.timetracker.i18n;

import javax.annotation.Nonnull;

public enum MessageKeys {

  SALDO_BETWEEN("saldo.between"),
  EMPLOYEE_SUMMARY("employee.summary"),
  EMPLOYEE_ID("employee.id"),
  EMPLOYEE_NAME("employee.name"),
  RFID("employee.rfid"),
  EMPLOYEE_FIRSTNAME("employee.firstname"),
  EMPLOYEE_LASTNAME("employee.lastname"),
  TIME_JOURNAL_SUMMARY("time.journal.summary"),
  TIME_JOURNAL_DETAILS("time.journal.details");

  private final String key;

  MessageKeys(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public String key() {
    return key;
  }
}
