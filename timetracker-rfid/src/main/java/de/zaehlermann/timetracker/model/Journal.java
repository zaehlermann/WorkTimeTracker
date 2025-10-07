package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class Journal {

  public static final String SPLIT_LINE = "==================================================" + System.lineSeparator();
  private final Employee employee;
  private final List<Workday> workdays;

  public Journal(@Nonnull final Employee employee,
                 @Nonnull final List<Absence> absences,
                 @Nonnull final List<RfidScan> allScans,
                 @Nonnull final Integer year,
                 @Nonnull final Integer month) {
    this.employee = employee;

    final Map<LocalDate, List<RfidScan>> scansOfTheDay = allScans.stream()
      .collect(Collectors.groupingBy(RfidScan::getWorkday));

    // Ensure all days of the month are represented, even if there are no scans
    final LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
    final LocalDate endOfTheMonth = LocalDate.of(year, month + 1, 1);
    firstDayOfMonth.datesUntil(endOfTheMonth).forEach(d -> scansOfTheDay.putIfAbsent(d, List.of()));

    this.workdays = scansOfTheDay.entrySet().stream()
      .map(e -> createWorkDay(e, absences))
      .sorted(Comparator.comparing(Workday::getDay))
      .toList();
  }

  @Nonnull
  private static Workday createWorkDay(@Nonnull final Map.Entry<LocalDate, List<RfidScan>> e, @Nonnull final List<Absence> absences) {
    final boolean hasNoScans = e.getValue().isEmpty();
    final LocalDate day = e.getKey();
    final Absence absenceForDay = absences.stream()
      .filter(a -> a.isInDay(day))
      .findFirst()
      .orElse(null);
    return new Workday(day, absenceForDay,
                       hasNoScans ? null : e.getValue().getFirst().getScanTime(),
                       hasNoScans ? null : e.getValue().getLast().getScanTime());
  }

  @Nonnull
  public String printJournal() {
    final BigDecimal saldoSum = workdays.stream()
      .map(Workday::getSaldo)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    return employee.toJournalHeader() + System.lineSeparator() +
           SPLIT_LINE +
           Workday.HEADER_LINE + System.lineSeparator() +
           workdays.stream()
             .map(Workday::toTxtLine)
             .collect(Collectors.joining()) +
           SPLIT_LINE +
           "                                 " + saldoSum;
  }
}
