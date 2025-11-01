package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Journal {

  public static final String SPLIT_LINE = "==================================================" + System.lineSeparator();
  private final Employee employee;
  private final List<Workday> workdays;
  private final List<WorkModel> workModels;

  public Journal(@Nonnull final Employee employee,
                 @Nonnull final List<WorkModel> workModels,
                 @Nonnull final List<Absence> absences,
                 @Nonnull final List<Correction> corrections,
                 @Nonnull final List<RfidScan> allScans,
                 @Nonnull final Integer year,
                 @Nonnull final Integer month) {
    this.employee = employee;
    this.workModels = workModels;

    final Map<LocalDate, List<RfidScan>> scanByDay = allScans.stream()
      .collect(Collectors.groupingBy(RfidScan::getWorkday));

    final Map<LocalDate, List<Correction>> correctionsByDay = corrections.stream()
      .collect(Collectors.groupingBy(Correction::getWorkday));

    // Ensure all days of the month are represented, even if there are no scans
    final LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
    final LocalDate endOfTheMonth = YearMonth.of(year, month).atEndOfMonth().plusDays(1);
    firstDayOfMonth.datesUntil(endOfTheMonth).forEach(d -> scanByDay.putIfAbsent(d, List.of()));

    this.workdays = scanByDay.entrySet().stream()
      .map(dailyScans -> createWorkDay(dailyScans, absences, correctionsByDay, workModels))
      .sorted(Comparator.comparing(Workday::getDay))
      .toList();
  }

  @Nonnull
  private static Workday createWorkDay(@Nonnull final Map.Entry<LocalDate, List<RfidScan>> dailyScans,
                                       @Nonnull final List<Absence> absences,
                                       @Nonnull final Map<LocalDate, List<Correction>> correctionsByDay,
                                       @Nonnull final List<WorkModel> workModels) {

    final LocalDate day = dailyScans.getKey();

    final WorkModel workModelForTheDay = workModels.stream()
      .filter(m -> m.isInEffectOnDay(day))
      .findFirst()
      .orElse(WorkModel.DEFAULT_WORKMODEL);

    final Absence absenceForDay = absences.stream()
      .filter(a -> a.isInDay(day))
      .findFirst()
      .orElse(null);

    final Correction correctionOfTheDay = getCorrectionOfTheDay(correctionsByDay, day); //there should be only one correction per day
    final LocalTime login = getLogin(dailyScans, correctionOfTheDay);
    final LocalTime logout = getLogout(dailyScans, correctionOfTheDay);
    return new Workday(day, absenceForDay, login, logout, correctionOfTheDay != null, workModelForTheDay);
  }

  @Nullable
  private static Correction getCorrectionOfTheDay(@Nonnull final Map<LocalDate, List<Correction>> correctionsByDay,
                                                  @Nonnull final LocalDate day) {
    final List<Correction> dailyCorrection = correctionsByDay.getOrDefault(day, List.of());
    return dailyCorrection.isEmpty() ? null : dailyCorrection.getLast();
  }

  @Nullable
  private static LocalTime getLogin(@Nonnull final Map.Entry<LocalDate, List<RfidScan>> dailyScans,
                                    @Nullable final Correction correctionOfTheDay) {

    if(correctionOfTheDay != null && correctionOfTheDay.getLogin() != null) {
      return correctionOfTheDay.getLogin();
    }
    return dailyScans.getValue().isEmpty() ? null : dailyScans.getValue().getFirst().getScanTime();
  }

  @Nullable
  private static LocalTime getLogout(@Nonnull final Map.Entry<LocalDate, List<RfidScan>> dailyScans,
                                     @Nullable final Correction correctionOfTheDay) {

    if(correctionOfTheDay != null && correctionOfTheDay.getLogout() != null) {
      return correctionOfTheDay.getLogout();
    }
    return dailyScans.getValue().isEmpty() ? null : dailyScans.getValue().getLast().getScanTime();
  }

  @Nonnull
  public List<JournalSummaryItem> getJournalSummaryItems() {
    final List<JournalSummaryItem> headers = new ArrayList<>(employee.toJournalSummaryHeaders(workModels));
    headers.add(new JournalSummaryItem("Hours Total", calcHoursTotal()));
    headers.add(new JournalSummaryItem("Saldo Total", calcSaldoTotal()));
    return headers;
  }

  @Nonnull
  public String printJournalTxt() {
    final String hoursTotal = calcHoursTotal();
    final String saldoTotal = calcSaldoTotal();
    return employee.toJournalTxtHeader(workModels) + System.lineSeparator() +
           SPLIT_LINE +
           Workday.HEADER_LINE_TXT + System.lineSeparator() +
           workdays.stream()
             .map(Workday::toTxtLine)
             .collect(Collectors.joining()) +
           SPLIT_LINE +
           "                                     " + hoursTotal + "  " + saldoTotal;
  }

  @Nonnull
  public String printJournalCsv() {
    final String hoursTotal = calcHoursTotal();
    final String saldoTotal = calcSaldoTotal();
    return employee.toJournalTxtHeader(workModels) + System.lineSeparator() +
           SPLIT_LINE +
           Workday.HEADER_LINE_CSV + System.lineSeparator() +
           workdays.stream()
             .map(Workday::toCsvLine)
             .collect(Collectors.joining()) +
           SPLIT_LINE +
           "Total Hours:" + hoursTotal + System.lineSeparator() +
           "Total Saldo:" + saldoTotal;
  }

  @Nonnull
  private String calcHoursTotal() {
    final Duration duration = Duration.of(workdays.stream()
                                            .map(workday -> workday.getHoursDayInPlace().toMinutes())
                                            .mapToLong(value -> value)
                                            .sum(), ChronoUnit.MINUTES);
    return formatDurationHHHmm(duration);
  }

  @Nonnull
  private String calcSaldoTotal() {
    final BigDecimal totalSaldo = workdays.stream()
      .map(Workday::getSaldo)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    return String.format("%+.2f", totalSaldo);
  }

  @Nonnull
  public static String formatDurationHHHmm(@Nonnull final Duration duration) {
    final long hours = duration.toHours();
    final long minutes = duration.minusHours(hours).toMinutes();
    return String.format("%03d:%02d", hours, minutes);
  }

  @Nonnull
  public List<Workday> getWorkdays() {
    return workdays;
  }

  public int getTotalDays() {
    return workdays.size();
  }

  @Nonnull
  public String getTotalSaldo() {
    return calcSaldoTotal();
  }

  @Nonnull
  public String getTotalHours() {
    return calcHoursTotal();
  }

  public long getTotalAbsenceDays() {
    return workdays.stream()
      .filter(w -> w.getAbsenceType() != null)
      .count();
  }

  public long getTotalCorrectedDays() {
    return workdays.stream()
      .filter(Workday::isCorrected)
      .count();
  }

  public long getTotalLogins() {
    return workdays.stream()
      .filter(w -> w.getLogin() != null)
      .count();
  }

  public long getTotalLogouts() {
    return workdays.stream()
      .filter(w -> w.getLogout() != null)
      .count();
  }
}
