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

  public Journal(final Employee employee, final List<RfidScan> allScans) {
    this.employee = employee;

    final Map<LocalDate, List<RfidScan>> scansOfTheDay = allScans.stream()
      .collect(Collectors.groupingBy(RfidScan::getWorkday));

    this.workdays = scansOfTheDay.entrySet().stream()
      .map(Journal::createWorkDay)
      .sorted(Comparator.comparing(Workday::getDay))
      .toList();
  }

  private static Workday createWorkDay(final Map.Entry<LocalDate, List<RfidScan>> e) {
    return new Workday(e.getKey(), e.getValue().getFirst().getScanTime(), e.getValue().getLast().getScanTime());
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
