package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * One closed period of absence.
 * a.k.a. Abwesenheit
 */
public class Absence implements AbstractCsvEntity<Employee> {
  public static final String HEADER_LINE = "RFID;TYPE;STARTDAY;ENDDAY;STARTTIME;ENDTIME";

  private final String employeeId;
  private final AbsenceType type;
  private final LocalDate startDay;

  /**
   * null means the full start day only
   */
  private final LocalDate endDay;

  /**
   * Start/End null means the full day;
   */
  private final LocalTime startTime;
  /**
   * Start/End null means the full day;
   */
  private final LocalTime endTime;

  public Absence(final String employeeId, final AbsenceType type,
                 final LocalDate startDay, final LocalDate endDay,
                 final LocalTime startTime, final LocalTime endTime) {
    this.employeeId = employeeId;
    this.type = type;
    this.startDay = startDay;
    this.endDay = endDay;
    this.startTime = startTime;
    this.endTime = endTime;
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

  public LocalDate getEndDay() {
    return endDay;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public LocalDate getStartDay() {
    return startDay;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public AbsenceType getType() {
    return type;
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
}
