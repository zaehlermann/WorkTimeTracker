package de.zaehlermann.timetracker.service;

import java.util.List;

import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.model.Journal;
import de.zaehlermann.timetracker.model.RfidScan;
import de.zaehlermann.timetracker.repository.AbsenceRepository;
import de.zaehlermann.timetracker.repository.EmployeeRepository;
import de.zaehlermann.timetracker.repository.JournalRepository;
import de.zaehlermann.timetracker.repository.RfidScanRepository;

public class JournalService {

  private final RfidScanRepository rfidScanRepository = new RfidScanRepository();
  private final JournalRepository journalRepository = new JournalRepository();
  private final EmployeeRepository employeeRepository = new EmployeeRepository();
  private final AbsenceRepository absenceRepository = new AbsenceRepository();

  public String createJournal(final String employeeId, final Integer year, final Integer month) {
    final Employee employee = employeeRepository.findEmployeeByEmployeeId(employeeId);
    final List<Absence> absences = absenceRepository.findAbsencesByEmployeeId(employeeId, year, month);
    final List<RfidScan> allScans = rfidScanRepository.findAllRfIdScansByRfid(employee.getRfid(), year, month);
    final String journal = new Journal(employee, absences, allScans, year, month).printJournal();
    journalRepository.saveToFile(employeeId, journal);
    return journal;
  }

  public List<String> getAllEmployeeNames() {
    return employeeRepository.findAll().stream()
      .map(Employee::getEmployeeId)
      .toList();
  }
}
