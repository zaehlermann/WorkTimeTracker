package de.zaehlermann.timetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Journal {

  public static final String SPLIT_LINE = "==================================================" + System.lineSeparator();
  private final Employee employee;
  private final List<Workday> workdays;

  public Journal(final Employee employee, final List<RfidScan> allScans, final Integer year, final Integer month) {
    this.employee = employee;

    final Map<LocalDate, List<RfidScan>> scansOfTheDay = allScans.stream()
      .collect(Collectors.groupingBy(RfidScan::getWorkday));

    // Ensure all days of the month are represented, even if there are no scans
    final LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
    final LocalDate endOfTheMonth = LocalDate.of(year, month + 1, 1);
    firstDayOfMonth.datesUntil(endOfTheMonth).forEach(d -> scansOfTheDay.putIfAbsent(d, List.of()));

    this.workdays = scansOfTheDay.entrySet().stream()
      .map(Journal::createWorkDay)
      .sorted(Comparator.comparing(Workday::getDay))
      .toList();
  }

  private static Workday createWorkDay(final Map.Entry<LocalDate, List<RfidScan>> e) {
    final boolean hasNoScans = e.getValue().isEmpty();
    return new Workday(e.getKey(),
                       hasNoScans ? null : e.getValue().getFirst().getScanTime(),
                       hasNoScans ? null : e.getValue().getLast().getScanTime());
  }

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
