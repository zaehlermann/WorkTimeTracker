package de.zaehlermann.timetracker.i18n;

import jakarta.annotation.Nonnull;

public enum MessageKeys {

  SALDO_BETWEEN("saldo.between"),
  EMPLOYEE_SUMMARY("employee.summary"),
  EMPLOYEE_ID("employee.id"),
  EMPLOYEE_NAME("employee.name"),
  RFID("employee.rfid"),
  EMPLOYEE_FIRSTNAME("employee.firstname"),
  EMPLOYEE_LASTNAME("employee.lastname"),

  TIME_JOURNAL_SUMMARY("time.journal.summary"),
  TIME_JOURNAL_DETAILS("time.journal.details"),
  TIME_JOURNAL_YEAR("time.journal.year"),
  TIME_JOURNAL_MONTH("time.journal.month"),

  WORKDAY_DATE("workday.date"),
  WORKDAY_WEEKDAY("workday.weekday"),
  WORKDAY_ABSENCE("workday.absence"),
  WORKDAY_LOGIN("workday.login"),
  WORKDAY_LOGOUT("workday.logout"),
  WORKDAY_CORRECTION("workday.correction"),
  WORKDAY_HOURS("workday.hours"),
  WORKDAY_SALDO("workday.saldo"),

  DAILY_WORKTIME("workmodel.daily_worktime"),
  DAILY_BREAKTIME("workmodel.daily_breaktime"),
  WORKMODEL_INITIAL_HOURS("workmodel.initialhours"),
  WORKMODEL_VALID_FROM("workmodel.valid_from"),
  WORKMODEL_VALID_UNTIL("workmodel.valid_until");

  private final String key;

  MessageKeys(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public String key() {
    return key;
  }

  @Nonnull
  public String getTranslation() {
    return Messages.get(this);
  }
}
