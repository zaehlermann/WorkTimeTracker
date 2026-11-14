package de.zaehlermann.timetracker.model;

import de.zaehlermann.timetracker.globals.TimeFormat;
import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;
import de.zaehlermann.timetracker.i18n.SupportedLocales;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;

public class Workday {

  public static final String HEADER_LINE_TXT = "DATE        D  OFF  LOGIN  LOGOUT  C  HOURS  SALDO";
  public static final List<String> HEADER_LINE_PDF =
    List.of(
      Messages.get(MessageKeys.WORKDAY_DATE),
      Messages.get(MessageKeys.WORKDAY_WEEKDAY),
      Messages.get(MessageKeys.WORKDAY_ABSENCE),
      Messages.get(MessageKeys.WORKDAY_LOGIN),
      Messages.get(MessageKeys.WORKDAY_LOGOUT),
      Messages.get(MessageKeys.WORKDAY_CORRECTION),
      Messages.get(MessageKeys.WORKDAY_HOURS),
      Messages.get(MessageKeys.WORKDAY_SALDO)
    );
  public static final String HEADER_LINE_CSV = String.join(";", HEADER_LINE_PDF);

  private final LocalDate day;
  private final LocalTime login;
  private final LocalTime logout;
  private final boolean corrected;
  private final Duration hoursDayInPlace;
  private final AbsenceType absenceType;
  private final BigDecimal saldo;

  public Workday(@NonNull final LocalDate day,
                 @Nullable final Absence absences,
                 @Nullable final LocalTime login,
                 @Nullable final LocalTime logout,
                 final boolean corrected,
                 @NonNull final WorkModel currentContract) {
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
    if (isWeekend(day)) {
      return AbsenceType.WEEKEND;
    }
    return absences != null ? absences.type() : null;
  }

  private static boolean isWeekend(final LocalDate day) {
    return List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(day.getDayOfWeek());
  }

  private long calcSaldoInMinutes(@Nullable final Duration hoursDayInPlace, @NonNull final WorkModel currentContract) {
    if (hoursDayInPlace == null) return 0;
    return absenceType != null ? this.hoursDayInPlace.toMinutes()
      : (this.hoursDayInPlace.toMinutes() - currentContract.getExpectedPresenceTimeInMins());
  }

  @NonNull
  private static Duration calcHoursInPlace(final LocalTime login, final LocalTime logout) {
    return login != null && logout != null ? Duration.between(login, logout) : Duration.ZERO;
  }

  @NonNull
  public List<String> toPdfRow() {
    return List.of(day.toString(),
      getWeekDayName(),
      (absenceType != null ? absenceType.getPrintValueLong() : ""),
      (login != null ? login.format(TimeFormat.TIME_FORMAT) : ""),
      (logout != null ? logout.format(TimeFormat.TIME_FORMAT) : ""),
      (corrected ? "X" : ""),
      getHoursDayInPlaceFormatted(),
      getSaldoFormatted());
  }

  @NonNull
  public String toTxtLine() {
    return day + "  " +
      getWeekDayValue() + "  " +
      String.format("%-3s", absenceType != null ? absenceType.getPrintValueShort() : "") + "  " +
      String.format("%-5s", login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + "  " +
      String.format("%-6s", logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + "  " +
      String.format("%-1s", corrected ? "X" : "") + "  " +
      getHoursDayInPlaceFormatted() + "  " +
      getSaldoFormatted() +
      System.lineSeparator();
  }

  @NonNull
  public String toCsvLine() {
    return day + ";" +
      getWeekDayValue() + ";" +
      (absenceType != null ? absenceType.getPrintValueLong() : "") + ";" +
      (login != null ? login.format(TimeFormat.TIME_FORMAT) : "") + ";" +
      (logout != null ? logout.format(TimeFormat.TIME_FORMAT) : "") + ";" +
      (corrected ? "X" : "") + ";" +
      getHoursDayInPlaceFormatted() + ";" +
      getSaldoFormatted() +
      System.lineSeparator();
  }

  @NonNull
  public LocalDate getDay() {
    return day;
  }

  public int getWeekDayValue() {
    return day.getDayOfWeek().getValue();
  }

  @NonNull
  public String getWeekDayName() {
    return day.getDayOfWeek().getDisplayName(TextStyle.SHORT, SupportedLocales.getDefault());
  }

  @Nullable
  public AbsenceType getAbsenceType() {
    return absenceType;
  }

  @Nullable
  public String getAbsenceTypePrintValueLong() {
    return absenceType == null ? null : absenceType.getPrintValueLong();
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

  @NonNull
  public Duration getHoursDayInPlace() {
    return hoursDayInPlace;
  }

  @NonNull
  public String getHoursDayInPlaceFormatted() {
    return String.format("%02d", hoursDayInPlace.toHoursPart()) + ":" + String.format("%02d", hoursDayInPlace.toMinutesPart());
  }

  @NonNull
  public BigDecimal getSaldo() {
    return saldo;
  }

  @NonNull
  public String getSaldoFormatted() {
    return TimeFormat.formatHoursHHmm(getSaldo());
  }

  public boolean isInSelectedRange(@NonNull final Integer selectedYear, @Nullable final Integer selectedMonth) {
    return selectedMonth == null ? day.getYear() == selectedYear
      : day.getYear() == selectedYear && day.getMonthValue() == selectedMonth;
  }

  public boolean isInDateRange(@NonNull final LocalDate start, @NonNull final LocalDate end) {
    return (day.isEqual(start) || day.isAfter(start)) && (day.isEqual(end) || day.isBefore(end));
  }

}
