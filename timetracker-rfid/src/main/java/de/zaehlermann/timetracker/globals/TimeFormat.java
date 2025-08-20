package de.zaehlermann.timetracker.globals;

import java.time.format.DateTimeFormatter;

public class TimeFormat {

  private TimeFormat() {}

  public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
}
