package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * One closed period of absence.
 * a.k.a. Abwesenheit
 */
public record Absence(String employeeId, AbsenceType type, LocalDate startDay, LocalDate endDay, LocalTime startTime, LocalTime endTime)
  implements AbstractCsvEntity<Absence> {
  public static final String HEADER_LINE = "EMPLOYEE;TYPE;STARTDAY;ENDDAY;STARTTIME;ENDTIME";

  /**
   * @param endDay null means the full start day only
   * @param startTime Start/End null means the full day;
   * @param endTime Start/End null means the full day;
   */
  public Absence(@Nonnull final String employeeId,
                 @Nonnull final AbsenceType type,
                 @Nonnull final LocalDate startDay,
                 @Nullable final LocalDate endDay,
                 @Nullable final LocalTime startTime,
                 @Nullable final LocalTime endTime) {
    this.employeeId = employeeId;
    this.type = type;
    this.startDay = startDay;
    this.endDay = endDay;
    this.startTime = startTime;
    this.endTime = endTime;
  }

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

  @Nonnull
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

  @Nonnull
  public static Absence fromCsvLine(@Nonnull final String csvLine) {
    final String[] parts = csvLine.split(";", -1);
    final String employeeId = parts[0];
    final AbsenceType type = AbsenceType.valueOf(parts[1]);
    final LocalDate startDay = LocalDate.parse(parts[2]);
    final LocalDate endDay = parts[3] == null || parts[3].isEmpty() ? null : LocalDate.parse(parts[3]);
    final LocalTime startTime = parts[4] == null || parts[4].isEmpty() ? null : LocalTime.parse(parts[4]);
    final LocalTime endTime = parts[5] == null || parts[5].isEmpty() ? null : LocalTime.parse(parts[5]);
    return new Absence(employeeId, type, startDay, endDay, startTime, endTime);
  }

  @Override
  @Nonnull
  public String toString() {
    return "Absence[" +
           "employeeId=" + employeeId + ", " +
           "type=" + type + ", " +
           "startDay=" + startDay + ", " +
           "endDay=" + endDay + ", " +
           "startTime=" + startTime + ", " +
           "endTime=" + endTime + ']';
  }

  public boolean isInDay(@Nonnull final LocalDate day) {
    return isDateInSpan(day, startDay, endDay == null ? startDay : endDay);
  }

  public static boolean isDateInSpan(@Nullable final LocalDate date,
                                     @Nullable final LocalDate start,
                                     @Nullable final LocalDate end) {
    if(date == null || start == null || end == null) return false;
    return (date.isEqual(start) || date.isAfter(start)) &&
           (date.isEqual(end) || date.isBefore(end));
  }
}
