package de.zaehlermann.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Journal {

  public static final String SPLITLINE = "==================================================" + System.lineSeparator();
  private final Employee employee;
  private final List<Workday> workdays;

  public Journal(Employee employee, List<RfIdScan> allScans) {
    this.employee = employee;

    Map<LocalDate, List<RfIdScan>> scansOfTheDay = allScans.stream()
        .collect(Collectors.groupingBy(RfIdScan::getWorkday));

    this.workdays = scansOfTheDay.entrySet().stream()
        .map(Journal::createWorkDay)
        .sorted(Comparator.comparing(Workday::getDay))
        .collect(Collectors.toList());
  }

  private static Workday createWorkDay(Map.Entry<LocalDate, List<RfIdScan>> e) {
    return new Workday(e.getKey(), e.getValue().getFirst().getScanTime(), e.getValue().getLast().getScanTime());
  }

  public String printJournal() {
    BigDecimal saldoSum = workdays.stream()
        .map(Workday::getSaldo)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return employee.toString() + System.lineSeparator() +
        SPLITLINE +
        Workday.HEADER_LINE + System.lineSeparator() +
        workdays.stream()
            .map(Workday::toTxtLine)
            .collect(Collectors.joining()) +
        SPLITLINE +
        "\t\t\t\t\t\t\t\t\t" + saldoSum;
  }
}
