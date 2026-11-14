package de.zaehlermann.timetracker.globals;

import org.eclipse.jdt.annotation.NonNull;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class TimeFormat {

  private TimeFormat() {
  }

  public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  @NonNull
  public static String formatHoursHHHmm(@NonNull final BigDecimal hours) {
    return formatDurationHHHmm(Duration.ofMinutes(hours.multiply(BigDecimal.valueOf(60)).longValue()));
  }

  public static String formatHoursHHmm(@NonNull final BigDecimal hours) {
    return formatDurationHHmm(Duration.ofMinutes(hours.multiply(BigDecimal.valueOf(60)).longValue()));
  }

  @NonNull
  public static String formatDurationHHHmm(@NonNull final Duration duration) {
    return formatDuration(duration, "%s%03d:%02d"); // +125:30
  }

  @NonNull
  public static String formatDurationHHmm(@NonNull final Duration duration) {
    return formatDuration(duration, "%s%02d:%02d"); // +5:30
  }

  @NonNull
  private static String formatDuration(@NonNull final Duration duration, final String format) {
    final long minutes = duration.toMinutes();
    final String sign = minutes < 0 ? "-" : "+";
    final long totalMinutes = Math.abs(minutes);
    final long hours = totalMinutes / 60;
    final long minutesOfHour = totalMinutes % 60;
    return String.format(format, sign, hours, minutesOfHour);
  }
}
