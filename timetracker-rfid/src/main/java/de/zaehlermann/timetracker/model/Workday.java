package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import de.zaehlermann.timetracker.globals.TimeFormat;

public class Workday {

  private static final int MUST_WORK_MINUTES_A_DAY = 8 * 60;
  private final LocalDate day;
  private final LocalTime login;
  private final LocalTime logout;
  private final Duration hoursDayInPlace;
  private final AbsenceType absenceType;
  private final BigDecimal saldo;

  public Workday(final LocalDate day, final LocalTime login, final LocalTime logout) {
    this.day = day;
    this.login = login;
    this.logout = logout;
    this.hoursDayInPlace = calcHoursInPlace(login, logout);
    this.absenceType = calcAbsenceType(day);
    final long saldoInMinutes = calcSaldoInMinutes(hoursDayInPlace);
    this.saldo = BigDecimal.valueOf(saldoInMinutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
  }

  private long calcSaldoInMinutes(final Duration hoursDayInPlace) {
    if(hoursDayInPlace == null) return 0;
    return absenceType != null ? this.hoursDayInPlace.toMinutes()
                               : (this.hoursDayInPlace.toMinutes() - MUST_WORK_MINUTES_A_DAY);
  }

  private static AbsenceType calcAbsenceType(final LocalDate day) {
    return List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day.getDayOfWeek()) ? AbsenceType.WEEKEND : null;
  }

  private static Duration calcHoursInPlace(final LocalTime login, final LocalTime logout) {
    return login != null && logout != null ? Duration.between(login, logout) : Duration.ZERO;
  }

  public LocalDate getDay() {
    return day;
  }

  public static final String HEADER_LINE = "DATE        D  OFF  LOGIN  LOGOUT  HOURS  SALDO";

  public String toTxtLine() {
    return day + "  " +
           day.getDayOfWeek().getValue() + "  " +
           String.format("%-3s", absenceType != null ? absenceType.getDisplay() : "") + "  " +
           String.format("%-5s", login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + "  " +
           String.format("%-6s", logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + "  " +
           String.format("%02d", hoursDayInPlace.toHoursPart()) + ":" +
           String.format("%02d", hoursDayInPlace.toMinutesPart()) + "  " +
           String.format("%+.2f", saldo) +
           System.lineSeparator();
  }

  public BigDecimal getSaldo() {
    return saldo;
  }
}
