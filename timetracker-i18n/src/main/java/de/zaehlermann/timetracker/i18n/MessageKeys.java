package de.zaehlermann.timetracker.i18n;

import javax.annotation.Nonnull;

public enum MessageKeys {

  EMPLOYEES("employees"),
  WORK_MODELS("work_models"),
  ABSENCE_TIMES("absence_times"),
  CORRECTION_TIMES("correction_times"),
  TIME_JOURNALS("time_journals"),
  HINTS("hints"),

  EMPLOYEE_SUMMARY("employee.summary"),
  EMPLOYEE_ID("employee.id"),
  EMPLOYEE_NAME("employee.name"),
  RFID("employee.rfid"),
  EMPLOYEE_FIRSTNAME("employee.firstname"),
  EMPLOYEE_LASTNAME("employee.lastname"),

  TIME_JOURNAL_SELECT("time.journal.select"),
  TIME_JOURNAL_SUMMARY("time.journal.summary"),
  TIME_JOURNAL_DETAILS("time.journal.details"),
  TIME_JOURNAL_YEAR("time.journal.year"),
  TIME_JOURNAL_MONTH("time.journal.month"),
  TIME_JOURNAL_ALL("time.journal.all"),
  TIME_JOURNAL_DESCRIPTION("time.journal.description"),
  TIME_JOURNAL_VALUE("time.journal.value"),
  TIME_JOURNAL_DOWNLOAD("time.journal.download"),
  TIME_JOURNAL_DOWNLOAD_PDF("time.journal.download.pdf"),
  TIME_JOURNAL_DOWNLOAD_TXT("time.journal.download.txt"),
  TIME_JOURNAL_DOWNLOAD_CSV("time.journal.download.csv"),
  TIME_JOURNAL_DOWNLOAD_FULL_BACKUP("time.journal.download.full_backup"),
  TIME_JOURNAL_SHOW_JOURNAL("time.journal.show_journal"),
  TIME_JOURNALS_SALDO_BETWEEN("time.journal.saldo.between"),
  TIME_JOURNAL_VACATION_REST("time.journal.vacation_rest"),
  TIME_JOURNAL_DATE("time.journal.date"),
  TIME_JOURNAL_WEEKDAY("time.journal.weekday"),
  TIME_JOURNAL_ABSENCE("time.journal.absence"),
  TIME_JOURNAL_LOGIN("time.journal.login"),
  TIME_JOURNAL_LOGOUT("time.journal.logout"),
  TIME_JOURNAL_CORRECTION("time.journal.correction"),
  TIME_JOURNAL_HOURS("time.journal.hours"),
  TIME_JOURNAL_SALDO("time.journal.saldo"),

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
  WORKMODEL_VALID_UNTIL("workmodel.valid_until"),
  WORKMODEL_DAILY_WORKTIME_IN_MIN("workmodel.daily_worktime_in_min"),
  WORKMODEL_DAILY_BREAKTIME_IN_MIN("workmodel.daily_breaktime_in_min"),
  WORKMODEL_DAILY_WORKTIME_IN_HHMM("workmodel.daily_worktime_in_hhmm"),
  WORKMODEL_DAILY_BREAKTIME_IN_HHMM("workmodel.daily_breaktime_in_hhmm"),

  SAVE("save"),
  DELETE("delete"),

  ABSENCE_START_DATE("absence.start_date"),
  ABSENCE_END_DATE("absence.end_date"),
  ABSENCE_START_TIME("absence.start_time"),
  ABSENCE_END_TIME("absence.end_time"),
  ABSENCE_TYPE("absence.type"),
  ABSENCE_TIMES_DESCRIPTION_0("absence.description_0"),
  ABSENCE_TIMES_DESCRIPTION_1("absence.description_1"),
  ABSENCE_TIMES_DESCRIPTION_2("absence.description_2"),
  ABSENCE_TIMES_DESCRIPTION_3("absence.description_3"),

  CORRECTION_TIMES_DATE("correction_times.date"),
  CORRECTION_TIMES_LOGIN_CORRECTION("correction_times.login_correction"),
  CORRECTION_TIMES_LOGOUT_CORRECTION("correction_times.logout_correction"),

  VACATIONS("vacations"),
  VACATION_YEAR("vacation.year"),
  VACATION_IN_DAYS("vacation.in_days"),
  VACATION_SAVED("vacation.saved"),
  VACATIONS_CURRENT_YEAR("vacations.current_year"),
  VACATION_DESCRIPTION("vacation.description"),

  MAIN("main"),
  MAIN_WELCOME_0("main.welcome_0"),
  MAIN_WELCOME_1("main.welcome_1"),
  MAIN_WELCOME_2("main.welcome_2"),
  MAIN_WELCOME_3("main.welcome_3"),
  MAIN_WELCOME_4("main.welcome_4"),
  MAIN_WELCOME_5("main.welcome_5");
  private final String key;

  MessageKeys(@Nonnull final String key) {
    this.key = key;
  }

  /**
   * Because Menu annotations need a static translation during compile time, the translation is exchanged this way.
   * It is not very elegant but works without a mambo jumbo.
   * @param title the title of the menu item annotation
   * @return the translated title
   */
  public static String getMainMenueTitle(final String title) {
    return switch(title) {
      case "Employees" -> MessageKeys.EMPLOYEES.getTranslation();
      case "Work models" -> MessageKeys.WORK_MODELS.getTranslation();
      case "Vacations" -> MessageKeys.VACATIONS.getTranslation();
      case "Absence times" -> MessageKeys.ABSENCE_TIMES.getTranslation();
      case "Correction times" -> MessageKeys.CORRECTION_TIMES.getTranslation();
      case "Time journals" -> MessageKeys.TIME_JOURNALS.getTranslation();
      default -> title;
    };
  }

  @Nonnull
  public String key() {
    return key;
  }

  @Nonnull
  public String getTranslation() {
    return Messages.get(this);
  }

  public String getTranslation(@Nonnull final Object... arg) {
   return Messages.get(this, arg);
  }
}
