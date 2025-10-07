package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkingContract {

  private String employeeId = "max.mustermann";
  private LocalDate validFrom = LocalDate.of(2025, 8, 1);
  private LocalDate validUntil; // null means unlimited
  private int contractedHoursAWeek = 40;

  // example work day: 8h a day, 45min break, start at 8, means finish at 16:45
  private LocalTime coreTimeStart = LocalTime.of(8, 0);
  private LocalTime coreTimeEnd = LocalTime.of(15, 0);
  private LocalTime flexTimeStart = LocalTime.of(6, 0);
  private LocalTime flexTimeEnd = LocalTime.of(18, 0);
  private int minutesOfBreakADay = 45;
}
