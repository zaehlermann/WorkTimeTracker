package de.zaehlermann.timetracker.model;

import de.zaehlermann.timetracker.i18n.MessageKeys;
import org.eclipse.jdt.annotation.NonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class Vacation implements AbstractCsvEntity<Vacation> {

  public static final String HEADER_LINE = "EMPLOYEE;YEAR;VACATION_IN_DAYS";
  public static final Vacation DEFAULT_VACATION = new Vacation("", LocalDate.now(ZoneId.systemDefault()).getYear(), 30);

  private final String employeeId;
  private final int year;
  private final int vacationInDays;

  public Vacation(@NonNull final String employeeId,
                  final int year,
                  final int vacationInDays) {
    this.employeeId = employeeId;
    this.year = year;
    this.vacationInDays = vacationInDays;
  }

  public Vacation(final String employeeId, final String year, final String vacationInDays) {
    this.employeeId = employeeId;
    this.year = Integer.parseInt(year);
    this.vacationInDays = Integer.parseInt(vacationInDays);
  }

  @NonNull
  @Override
  public String toCsvLine() {
    return employeeId + ";" +
           year + ";" +
           vacationInDays + System.lineSeparator();
  }

  @NonNull
  public static Vacation fromCsvLine(@NonNull final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    return new Vacation(split[0], split[1], split[2]);
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public int getYear() {
    return year;
  }

  public int getVacationInDays() {
    return vacationInDays;
  }

  @Override
  public boolean equals(final Object o) {
    if(!(o instanceof final Vacation that)) return false;
    return Objects.equals(employeeId, that.employeeId) && Objects.equals(year, that.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, year);
  }

  @NonNull
  public List<JournalSummaryItem> getSummaryItems() {
    return List.of(
      new JournalSummaryItem(MessageKeys.VACATIONS_CURRENT_YEAR.getTranslation(String.valueOf(year)), vacationInDays + "d")
    );
  }
}
