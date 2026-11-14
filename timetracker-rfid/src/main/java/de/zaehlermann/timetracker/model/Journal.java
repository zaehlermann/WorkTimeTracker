package de.zaehlermann.timetracker.model;

import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;
import de.zaehlermann.timetracker.pdf.builder.PdfBuilder;
import de.zaehlermann.timetracker.pdf.builder.PdfTable;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.zaehlermann.timetracker.globals.TimeFormat.formatDurationHHHmm;
import static de.zaehlermann.timetracker.globals.TimeFormat.formatHoursHHHmm;

@NonNullByDefault
public class Journal {

  private final Employee employee;
  private final List<Workday> workdays;
  private final List<WorkModel> workModels;
  private final List<Vacation> vacations;
  private final List<Absence> absences;
  private final Integer selectedYear;
  @Nullable
  private final Integer selectedMonth;

  public Journal(final Employee employee,
                 final List<WorkModel> workModels,
                 final List<Vacation> vacations,
                 final List<Absence> absences,
                 final List<Correction> corrections,
                 final List<RfidScan> allScans,
                 final Integer selectedYear,
                 @Nullable final Integer selectedMonth) {
    this.employee = employee;
    this.workModels = workModels;
    this.vacations = vacations;
    this.absences = absences;
    this.selectedYear = selectedYear;
    this.selectedMonth = selectedMonth;

    final Map<LocalDate, List<RfidScan>> scanByDay = allScans.stream()
      .collect(Collectors.groupingBy(RfidScan::getWorkday));

    final Map<LocalDate, List<Correction>> correctionsByDay = corrections.stream()
      .collect(Collectors.groupingBy(Correction::getWorkday));

    // Ensure all days are represented, even if there are no scans
    final LocalDate journalStart = workModels.getFirst().getValidFrom();
    final LocalDate selectedEnd = YearMonth.of(Year.now(ZoneId.systemDefault()).getValue(), Month.DECEMBER).atEndOfMonth().plusDays(1);
    if (journalStart.isBefore(selectedEnd)) {
      journalStart.datesUntil(selectedEnd).forEach(d -> scanByDay.putIfAbsent(d, List.of()));
    }

    this.workdays = scanByDay.entrySet().stream()
      .map(dailyScans -> createWorkDay(dailyScans, absences, correctionsByDay, workModels))
      .sorted(Comparator.comparing(Workday::getDay))
      .toList();
  }

  private static Workday createWorkDay(final Map.Entry<LocalDate, List<RfidScan>> dailyScans,
                                       final List<Absence> absences,
                                       final Map<LocalDate, List<Correction>> correctionsByDay,
                                       final List<WorkModel> workModels) {

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
  private static Correction getCorrectionOfTheDay(final Map<LocalDate, List<Correction>> correctionsByDay,
                                                  final LocalDate day) {
    final List<Correction> dailyCorrection = correctionsByDay.getOrDefault(day, List.of());
    return dailyCorrection.isEmpty() ? null : dailyCorrection.getLast();
  }

  @Nullable
  private static LocalTime getLogin(final Map.Entry<LocalDate, List<RfidScan>> dailyScans,
                                    @Nullable final Correction correctionOfTheDay) {

    if (correctionOfTheDay != null && correctionOfTheDay.getLogin() != null) {
      return correctionOfTheDay.getLogin();
    }
    return dailyScans.getValue().isEmpty() ? null : dailyScans.getValue().getFirst().getScanTime();
  }

  @Nullable
  private static LocalTime getLogout(final Map.Entry<LocalDate, List<RfidScan>> dailyScans,
                                     @Nullable final Correction correctionOfTheDay) {

    if (correctionOfTheDay != null && correctionOfTheDay.getLogout() != null) {
      return correctionOfTheDay.getLogout();
    }
    return dailyScans.getValue().isEmpty() ? null : dailyScans.getValue().getLast().getScanTime();
  }

  public List<JournalSummaryItem> getEmployeeWorkModelSummaryItems() {
    final List<JournalSummaryItem> items = new ArrayList<>(getEmployeeSummaryItems());
    items.addAll(getLastWorkModelSummaryItems());
    items.addAll(getVacationSummaryItems());
    return items;
  }

  public List<JournalSummaryItem> getEmployeeSummaryItems() {
    return employee.getSummaryItems();
  }

  public List<JournalSummaryItem> getLastWorkModelSummaryItems() {
    return workModels.getLast().getSummaryItems();
  }

  private List<JournalSummaryItem> getVacationSummaryItems() {
    return vacations.stream()
      .map(Vacation::getSummaryItems)
      .flatMap(Collection::stream)
      .collect(Collectors.toList());
  }

  private static String getRangeDesc(final LocalDate start, final LocalDate end) {
    return MessageKeys.TIME_JOURNALS_SALDO_BETWEEN.getTranslation(start, end);
  }

  public void createJournalPdf(final File file, final JournalSummaryType journalSummaryType) {
    final List<List<String>> pdfTableRows = workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .map(Workday::toPdfRow)
      .collect(Collectors.toList());

    try {
      new PdfBuilder(file)
        .open()
        .addParagraph(printJournalHeader(journalSummaryType))
        .addTable(new PdfTable(pdfTableRows.getFirst().size())
          .addHeader(Workday.HEADER_LINE_PDF)
          .addRows(pdfTableRows))
        .build();
    } catch (final FileNotFoundException e) {
      throw new IllegalStateException(e);
    }

  }

  public String printJournalTxt(final JournalSummaryType journalSummaryType) {
    return printJournalHeader(journalSummaryType) +
      Workday.HEADER_LINE_TXT + System.lineSeparator() +
      workdays.stream()
        .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
        .map(Workday::toTxtLine)
        .collect(Collectors.joining());
  }

  public String printJournalCsv(final JournalSummaryType journalSummaryType) {
    return printJournalHeader(journalSummaryType) +
      Workday.HEADER_LINE_CSV + System.lineSeparator() +
      workdays.stream()
        .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
        .map(Workday::toCsvLine)
        .collect(Collectors.joining());
  }

  private String printJournalHeader(final JournalSummaryType journalSummaryType) {
    return "# " + Messages.get(MessageKeys.EMPLOYEE_SUMMARY) + System.lineSeparator() +
      getEmployeeWorkModelSummaryItems().stream()
        .map(JournalSummaryItem::toTxtLine)
        .collect(Collectors.joining()) +
      System.lineSeparator() +
      "# " + Messages.get(MessageKeys.TIME_JOURNAL_SUMMARY) + System.lineSeparator() +
      getJournalSummaryItems(journalSummaryType).stream()
        .map(JournalSummaryItem::toTxtLine)
        .collect(Collectors.joining()) +
      System.lineSeparator() +
      "# " + Messages.get(MessageKeys.TIME_JOURNAL_DETAILS) + System.lineSeparator();
  }

  public List<JournalSummaryItem> getJournalSummaryItems(final JournalSummaryType journalSummaryType) {
    final LocalDate today = LocalDate.now(ZoneId.systemDefault());
    final LocalDate firstWorkingDay = workdays.getFirst().getDay();
    final LocalDate firstOfSelectedMonth = LocalDate.of(selectedYear, selectedMonth == null ? 1 : selectedMonth, 1);
    final LocalDate endOfSelectedMonth = YearMonth.of(selectedYear, selectedMonth == null ? 12 : selectedMonth).atEndOfMonth();
    final String selectedRange = printSelectedRange();
    final BigDecimal initialHours = calcInitialHours();

    final List<JournalSummaryItem> items = new ArrayList<>();

    if (journalSummaryType == JournalSummaryType.ALL_DETAILS) {
      items.add(new JournalSummaryItem(Messages.get(MessageKeys.WORKMODEL_INITIAL_HOURS), formatHoursHHHmm(initialHours)));
    }

    items.add(new JournalSummaryItem(Messages.get(MessageKeys.WORKDAY_HOURS) + " " + selectedRange,
      calcSelectedHours(selectedYear, selectedMonth)));

    if (journalSummaryType == JournalSummaryType.ALL_DETAILS) {
      items.add(new JournalSummaryItem(getRangeDesc(firstOfSelectedMonth, endOfSelectedMonth),
        formatHoursHHHmm(calcSaldoInRange(firstOfSelectedMonth, endOfSelectedMonth))));
      items.add(new JournalSummaryItem(getRangeDesc(firstWorkingDay, today),
        formatHoursHHHmm(initialHours.add(calcSaldoInRange(firstWorkingDay, today)))));
      items.add(new JournalSummaryItem(getRangeDesc(firstWorkingDay, endOfSelectedMonth),
        formatHoursHHHmm(initialHours.add(calcSaldoInRange(firstWorkingDay, endOfSelectedMonth)))));
      items.add(new JournalSummaryItem(MessageKeys.TIME_JOURNAL_VACATION_REST.getTranslation(),
        calcVacationRest() + "d"));
    }

    return items;
  }

  private String calcVacationRest() {

    final int sumVacationsGiven = vacations.stream()
      .mapToInt(Vacation::getVacationInDays)
      .sum();

    final double sumVacationUsed = absences.stream()
      .filter(a -> a.getType() == AbsenceType.VACATION)
      .mapToDouble(Absence::getTimeRangeInDays)
      .sum();

    final double vacationRest = sumVacationsGiven - sumVacationUsed;
    return String.valueOf(vacationRest);
  }

  private BigDecimal calcInitialHours() {
    return workModels.stream()
      .map(w -> BigDecimal.valueOf(w.getInitialHours()))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private String calcSelectedHours(final Integer selectedYear, @Nullable final Integer selectedMonth) {
    final long sum = workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .map(workday -> workday.getHoursDayInPlace().toMinutes())
      .mapToLong(value -> value)
      .sum();
    return formatDurationHHHmm(Duration.of(sum, ChronoUnit.MINUTES));
  }

  private BigDecimal calcSaldoInRange(final LocalDate start, final LocalDate end) {
    return workdays.stream()
      .filter(w -> w.isInDateRange(start, end))
      .map(Workday::getSaldo)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private String calcSelectedSaldo(final Integer selectedYear, @Nullable final Integer selectedMonth) {
    final BigDecimal totalSaldo = workdays.stream()
      .filter(w -> w.isInSelectedRange(selectedYear, selectedMonth))
      .map(Workday::getSaldo)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    return formatHoursHHHmm(totalSaldo);
  }

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

  public String getSelectedTotalSaldo() {
    return calcSelectedSaldo(selectedYear, selectedMonth);
  }

  public String getSelectedTotalHours() {
    return calcSelectedHours(selectedYear, selectedMonth);
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

  public String printSelectedRange() {
    return selectedYear + (selectedMonth == null ? "" : "-" + Month.of(selectedMonth).getValue());
  }

}
