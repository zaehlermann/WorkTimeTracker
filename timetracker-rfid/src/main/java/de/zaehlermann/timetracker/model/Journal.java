package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Journal {

  private final Employee employee;
  private final List<Workday> workdays;
  private final List<WorkModel> workModels;
  @Nonnull
  private final Integer selectedYear;
  @Nullable
  private final Integer selectedMonth;

  public Journal(@Nonnull final Employee employee,
                 @Nonnull final List<WorkModel> workModels,
                 @Nonnull final List<Absence> absences,
                 @Nonnull final List<Correction> corrections,
                 @Nonnull final List<RfidScan> allScans,
                 @Nonnull final Integer selectedYear,
                 @Nullable final Integer selectedMonth) {
    this.employee = employee;
    this.workModels = workModels;
    this.selectedYear = selectedYear;
    this.selectedMonth = selectedMonth;

    final Map<LocalDate, List<RfidScan>> scanByDay = allScans.stream()
      .collect(Collectors.groupingBy(RfidScan::getWorkday));

    final Map<LocalDate, List<Correction>> correctionsByDay = corrections.stream()
      .collect(Collectors.groupingBy(Correction::getWorkday));

    // Ensure all days are represented, even if there are no scans
    final LocalDate journalStart = workModels.getFirst().getValidFrom();
    final LocalDate journalEnd = YearMonth.of(selectedYear, selectedMonth == null ? 12 : selectedMonth).atEndOfMonth().plusDays(1);
    journalStart.datesUntil(journalEnd).forEach(d -> scanByDay.putIfAbsent(d, List.of()));

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
  public List<JournalSummaryItem> getEmployeeWorkModelSummaryItems() {
    final List<JournalSummaryItem> items = new ArrayList<>(getEmployeeSummaryItems());
    items.addAll(getLastWorkModelSummaryItems());
    return items;
  }

  @Nonnull
  public List<JournalSummaryItem> getEmployeeSummaryItems() {
    return employee.getSummaryItems();
  }

  @Nonnull
  public List<JournalSummaryItem> getLastWorkModelSummaryItems() {
    return workModels.getLast().getSummaryItems();
  }

  @Nonnull
  public List<JournalSummaryItem> getJournalSummaryItems() {
    final LocalDate today = LocalDate.now();
    final LocalDate firstWorkingDay = workdays.getFirst().getDay();
    final LocalDate firstOfSelectedMonth = LocalDate.of(selectedYear, selectedMonth == null ? 1 : selectedMonth, 1);
    final LocalDate endOfSelectedMonth = YearMonth.of(selectedYear, selectedMonth == null ? 12 : selectedMonth).atEndOfMonth();
    final String selectedRange = selectedYear
                                 + (selectedMonth == null ? "" : "-" + Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Locale.ENGLISH));

    return List.of(new JournalSummaryItem("Hours " + selectedRange, calcHoursTotalSelected(selectedYear, selectedMonth)),
                   new JournalSummaryItem(getRangeDesc(firstOfSelectedMonth, endOfSelectedMonth) + " (Selected range)",
                                          calcSaldoTotalRange(firstOfSelectedMonth, endOfSelectedMonth)),
                   new JournalSummaryItem(getRangeDesc(firstWorkingDay, today) + " (Total until today)",
                                          calcSaldoTotalRange(firstWorkingDay, today)),
                   new JournalSummaryItem(getRangeDesc(firstWorkingDay, endOfSelectedMonth) + " (Total until end of selected range)",
                                          calcSaldoTotalRange(firstWorkingDay, endOfSelectedMonth)));
  }

  @Nonnull
  private static String getRangeDesc(@Nonnull final LocalDate start, @Nonnull final LocalDate end) {
    return "Saldo between " + start + " and " + end;
  }

  @Nonnull
  public String printJournalTxt() {
    return "# Employee Summary" + System.lineSeparator() +
           getEmployeeWorkModelSummaryItems().stream()
             .map(JournalSummaryItem::toTxtLine)
             .collect(Collectors.joining()) +
           System.lineSeparator() +
           "# Time Journal Summary" + System.lineSeparator() +
           getJournalSummaryItems().stream()
             .map(JournalSummaryItem::toTxtLine)
             .collect(Collectors.joining()) +
           System.lineSeparator() +
           "# Time Journal Details" + System.lineSeparator() +
           Workday.HEADER_LINE_TXT + System.lineSeparator() +
           workdays.stream()
             .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
             .map(Workday::toTxtLine)
             .collect(Collectors.joining());
  }

  @Nonnull
  public String printJournalCsv() {
    return "# Employee Summary" + System.lineSeparator() +
           getEmployeeWorkModelSummaryItems().stream()
             .map(JournalSummaryItem::toTxtLine)
             .collect(Collectors.joining()) +
           System.lineSeparator() +
           "# Time Journal Summary" + System.lineSeparator() +
           getJournalSummaryItems().stream()
             .map(JournalSummaryItem::toTxtLine)
             .collect(Collectors.joining()) +
           System.lineSeparator() +
           "# Time Journal Details" + System.lineSeparator() +
           Workday.HEADER_LINE_CSV + System.lineSeparator() +
           workdays.stream()
             .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
             .map(Workday::toCsvLine)
             .collect(Collectors.joining());
  }

  @Nonnull
  private String calcHoursTotalSelected(@Nonnull final Integer selectedYear, @Nullable final Integer selectedMonth) {
    final Duration duration = Duration.of(workdays.stream()
                                            .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
                                            .map(workday -> workday.getHoursDayInPlace().toMinutes())
                                            .mapToLong(value -> value)
                                            .sum(), ChronoUnit.MINUTES);
    return formatDurationHHHmm(duration);
  }

  @Nonnull
  private String calcSaldoTotalRange(@Nonnull final LocalDate start, @Nonnull final LocalDate end) {
    final BigDecimal totalSaldo = workdays.stream()
      .filter(w -> w.isInDateRange(start, end))
      .map(Workday::getSaldo)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    return String.format("%+.2f", totalSaldo);
  }

  @Nonnull
  private String calcSaldoTotalSelected(@Nonnull final Integer selectedYear, @Nullable final Integer selectedMonth) {
    final BigDecimal totalSaldo = workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
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
  public List<Workday> getSelectedWorkdays() {
    return workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .toList();
  }

  public long getSelectedTotalDays() {
    return workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .count();
  }

  @Nonnull
  public String getSelectedTotalSaldo() {
    return calcSaldoTotalSelected(selectedYear, selectedMonth);
  }

  @Nonnull
  public String getSelectedTotalHours() {
    return calcHoursTotalSelected(selectedYear, selectedMonth);
  }

  public long getSelectedTotalAbsenceDays() {
    return workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .filter(w -> w.getAbsenceType() != null)
      .count();
  }

  public long getSelectedTotalCorrectedDays() {
    return workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .filter(Workday::isCorrected)
      .count();
  }

  public long getSelectedTotalLogins() {
    return workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .filter(w -> w.getLogin() != null)
      .count();
  }

  public long getSelectedTotalLogouts() {
    return workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .filter(w -> w.getLogout() != null)
      .count();
  }
}
