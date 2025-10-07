package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.zaehlermann.timetracker.globals.TimeFormat;

public class Workday {

  private static final int WORKTIME_A_DAY = 8 * 60;
  private static final int BREAKTIME_A_DAY = 30;
  private final LocalDate day;
  private final LocalTime login;
  private final LocalTime logout;
  private final boolean corrected;
  private final Duration hoursDayInPlace;
  private final AbsenceType absenceType;
  private final BigDecimal saldo;

  public Workday(@Nonnull final LocalDate day,
                 @Nullable final Absence absences,
                 @Nullable final LocalTime login,
                 @Nullable final LocalTime logout,
                 final boolean corrected) {
    this.day = day;

    this.absenceType = getAbsenceType(day, absences);
    this.login = login;
    this.logout = logout;
    this.corrected = corrected;
    this.hoursDayInPlace = calcHoursInPlace(login, logout);
    final long saldoInMinutes = calcSaldoInMinutes(hoursDayInPlace);
    this.saldo = BigDecimal.valueOf(saldoInMinutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
  }

  @Nullable
  private static AbsenceType getAbsenceType(final LocalDate day, final Absence absences) {
    if(isWeekend(day)) {
      return AbsenceType.WEEKEND;
    }
    return absences != null ? absences.type() : null;
  }

  private static boolean isWeekend(final LocalDate day) {
    return List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day.getDayOfWeek());
  }

  private long calcSaldoInMinutes(final Duration hoursDayInPlace) {
    if(hoursDayInPlace == null) return 0;
    return absenceType != null ? this.hoursDayInPlace.toMinutes()
                               : (this.hoursDayInPlace.toMinutes() - WORKTIME_A_DAY - BREAKTIME_A_DAY);
  }

  @Nonnull
  private static Duration calcHoursInPlace(final LocalTime login, final LocalTime logout) {
    return login != null && logout != null ? Duration.between(login, logout) : Duration.ZERO;
  }

  public static final String HEADER_LINE_TXT = "DATE        D  OFF  LOGIN  LOGOUT  C  HOURS  SALDO";
  public static final String HEADER_LINE_CSV = "DATE;D;OFF;LOGIN;LOGOUT;C;HOURS;SALDO";

  @Nonnull
  public String toTxtLine() {
    return day + "  " +
           day.getDayOfWeek().getValue() + "  " +
           String.format("%-3s", absenceType != null ? absenceType.getPrintValue() : "") + "  " +
           String.format("%-5s", login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + "  " +
           String.format("%-6s", logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + "  " +
           String.format("%-1s", corrected ? "X" : "") + "  " +
           String.format("%02d", hoursDayInPlace.toHoursPart()) + ":" + String.format("%02d", hoursDayInPlace.toMinutesPart()) + "  " +
           String.format("%+.2f", saldo) +
           System.lineSeparator();
  }

  @Nonnull
  public String toCsvLine() {
    return day + ";" +
           day.getDayOfWeek().getValue() + ";" +
           (absenceType != null ? absenceType.getPrintValue() : "") + ";" +
           (login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + ";" +
           (logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + ";" +
           (corrected ? "X" : "") + ";" +
           String.format("%02d", hoursDayInPlace.toHoursPart()) + ":" + String.format("%02d", hoursDayInPlace.toMinutesPart()) + ";" +
           String.format("%+.2f", saldo) +
           System.lineSeparator();
  }

  @Nonnull
  public LocalDate getDay() {
    return day;
  }

  @Nonnull
  public BigDecimal getSaldo() {
    return saldo;
  }

  @Nonnull
  public Duration getHoursDayInPlace() {
    return hoursDayInPlace;
  }
}
