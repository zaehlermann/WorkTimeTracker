package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * One closed period of absence.
 * a.k.a. Abwesenheit
 */
public class Absence implements AbstractCsvEntity<Employee> {
  public static final String HEADER_LINE = "RFID;TYPE;STARTDAY;ENDDAY;STARTTIME;ENDTIME";

  private final String rfid;
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

  public Absence(final String rfid, final AbsenceType type,
                 final LocalDate startDay, final LocalDate endDay,
                 final LocalTime startTime, final LocalTime endTime) {
    this.rfid = rfid;
    this.type = type;
    this.startDay = startDay;
    this.endDay = endDay;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @Override
  public String toCsvLine() {
    return rfid + ";" +
           type.name() + ";" +
           startDay.toString() + ";" +
           (endDay != null ? endDay.toString() : "") + ";" +
           (startTime != null ? startTime.toString() : "") + ";" +
           (endTime != null ? endTime.toString() : "") +
           System.lineSeparator();
  }
}
