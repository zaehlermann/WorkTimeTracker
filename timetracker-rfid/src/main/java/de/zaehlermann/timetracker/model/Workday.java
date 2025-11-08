package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.zaehlermann.timetracker.globals.TimeFormat;

public class Workday {

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
                 final boolean corrected,
                 @Nonnull final WorkModel currentContract) {
    this.day = day;
    this.absenceType = calcAbsenceType(day, absences);
    this.login = login;
    this.logout = logout;
    this.corrected = corrected;
    this.hoursDayInPlace = calcHoursInPlace(login, logout);
    final long saldoInMinutes = calcSaldoInMinutes(hoursDayInPlace, currentContract);
    this.saldo = BigDecimal.valueOf(saldoInMinutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
  }

  @Nullable
  private static AbsenceType calcAbsenceType(final LocalDate day, final Absence absences) {
    if(isWeekend(day)) {
      return AbsenceType.WEEKEND;
    }
    return absences != null ? absences.type() : null;
  }

  private static boolean isWeekend(final LocalDate day) {
    return List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day.getDayOfWeek());
  }

  private long calcSaldoInMinutes(@Nullable final Duration hoursDayInPlace, @Nonnull final WorkModel currentContract) {
    if(hoursDayInPlace == null) return 0;
    return absenceType != null ? this.hoursDayInPlace.toMinutes()
                               : (this.hoursDayInPlace.toMinutes() - currentContract.getExpectedPresenceTimeInMins());
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
           getWeekDayValue() + "  " +
           String.format("%-3s", absenceType != null ? absenceType.getPrintValue() : "") + "  " +
           String.format("%-5s", login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + "  " +
           String.format("%-6s", logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + "  " +
           String.format("%-1s", corrected ? "X" : "") + "  " +
           getHoursDayInPlaceFormatted() + "  " +
           String.format("%+.2f", saldo) +
           System.lineSeparator();
  }

  @Nonnull
  public String toCsvLine() {
    return day + ";" +
           getWeekDayValue() + ";" +
           (absenceType != null ? absenceType.getPrintValue() : "") + ";" +
           (login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + ";" +
           (logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + ";" +
           (corrected ? "X" : "") + ";" +
           getHoursDayInPlaceFormatted() + ";" +
           String.format("%+.2f", saldo) +
           System.lineSeparator();
  }

  @Nonnull
  public LocalDate getDay() {
    return day;
  }

  public int getWeekDayValue() {
    return day.getDayOfWeek().getValue();
  }

  @Nonnull
  public String getWeekDayName() {
    return day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
  }

  @Nullable
  public AbsenceType getAbsenceType() {
    return absenceType;
  }

  @Nullable
  public LocalTime getLogin() {
    return login;
  }

  @Nullable
  public LocalTime getLogout() {
    return logout;
  }

  public boolean isCorrected() {
    return corrected;
  }

  @Nonnull
  public Duration getHoursDayInPlace() {
    return hoursDayInPlace;
  }

  @Nonnull
  public String getHoursDayInPlaceFormatted() {
    return String.format("%02d", hoursDayInPlace.toHoursPart()) + ":" + String.format("%02d", hoursDayInPlace.toMinutesPart());
  }

  @Nonnull
  public BigDecimal getSaldo() {
    return saldo;
  }

}
