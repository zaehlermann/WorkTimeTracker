package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * One closed period of absence.
 * a.k.a. Abwesenheit
 * @param endDay null means the full start day only
 * @param startTime Start/End null means the full day;
 * @param endTime Start/End null means the full day;
 */
public record Absence(String employeeId, AbsenceType type, LocalDate startDay, LocalDate endDay, LocalTime startTime, LocalTime endTime)
  implements AbstractCsvEntity<Employee> {
  public static final String HEADER_LINE = "EMPLOYEE;TYPE;STARTDAY;ENDDAY;STARTTIME;ENDTIME";

  @Override
  public boolean equals(final Object o) {
    if(!(o instanceof final Absence absence)) return false;
    return Objects.equals(employeeId, absence.employeeId) && Objects.equals(startDay, absence.startDay) &&
           Objects.equals(endDay, absence.endDay) && Objects.equals(startTime, absence.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, startDay, endDay, startTime);
  }

  @Override
  public String toCsvLine() {
    return employeeId + ";" +
           type.name() + ";" +
           startDay.toString() + ";" +
           (endDay != null ? endDay.toString() : "") + ";" +
           (startTime != null ? startTime.toString() : "") + ";" +
           (endTime != null ? endTime.toString() : "") +
           System.lineSeparator();
  }

  public static Absence fromCsvLine(final String csvLine) {
    final String[] parts = csvLine.split(";",-1);
    final String employeeId = parts[0];
    final AbsenceType type = AbsenceType.valueOf(parts[1]);
    final LocalDate startDay = LocalDate.parse(parts[2]);
    final LocalDate endDay = parts[3] == null || parts[3].isEmpty() ? null : LocalDate.parse(parts[3]);
    final LocalTime startTime = parts[4] == null || parts[4].isEmpty() ? null : LocalTime.parse(parts[4]);
    final LocalTime endTime = parts[5] == null || parts[5].isEmpty() ? null : LocalTime.parse(parts[5]);
    return new Absence(employeeId, type, startDay, endDay, startTime, endTime);
  }
}
