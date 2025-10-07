package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * One closed period of absence.
 * a.k.a. Abwesenheit
 */
public class Absence {

  private String employeeId;
  private AbsenceType type = AbsenceType.SICKNESS;
  private LocalDate startDay = LocalDate.now();

  // null means the full start day only
  private LocalDate endDay;

  // Start/End null means the full day;
  private LocalTime start;
  private LocalTime end;


}
