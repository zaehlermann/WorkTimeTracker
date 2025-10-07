package de.zaehlermann.timetracker.service;

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

  public String createJournal(final String rfid, final Integer year, final Integer month) {
    final List<RfidScan> allScans = rfidScanRepository.findAllRfIdScansByRfid(rfid, year, month);
    final Employee employee = employeeRepository.findEmployee(rfid);
    final String journal = new Journal(employee, allScans).printJournal();
    journalRepository.saveToFile(rfid, journal);
    return journal;
  }

  public List<String> getAllEmployeeNames() {
    return rfidScanRepository.findAllRfids();
  }
}
