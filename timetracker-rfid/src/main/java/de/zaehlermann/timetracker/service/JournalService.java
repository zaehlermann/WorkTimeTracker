package de.zaehlermann.timetracker.service;

import java.io.IOException;
import java.util.List;

import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.model.Journal;
import de.zaehlermann.timetracker.model.RfidScan;
import de.zaehlermann.timetracker.repository.EmployeeRepository;
import de.zaehlermann.timetracker.repository.JournalRepository;
import de.zaehlermann.timetracker.repository.RfidScanRepository;

public class JournalService {

  private final RfidScanRepository rfidScanRepository = new RfidScanRepository();
  private final JournalRepository journalRepository = new JournalRepository();
  private final EmployeeRepository employeeRepository = new EmployeeRepository();

  public String createJournal(final String input) throws IOException {
    final List<RfidScan> allScans = rfidScanRepository.findAllRfIds(input);
    final Employee employee = employeeRepository.findEmployee(input);
    final String journal = new Journal(employee, allScans).printJournal();
    journalRepository.writeNewFile(input, journal);
    return journal;
  }
}
