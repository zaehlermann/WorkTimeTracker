package de.zaehlermann.timetracker.service;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

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

  @Nonnull
  public String createAndSaveJournalTxt(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Journal journal = createJournal(employeeId, year, month);
    final String journalTxt = journal.printJournalTxt();
    journalRepository.saveToTxtFile(employeeId, journalTxt);
    return journalTxt;
  }

  public File downloadCsv(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Journal journal = createJournal(employeeId, year, month);
    final String journalTxt = journal.printJournalCsv();
    return journalRepository.saveToCsvFile(employeeId, journalTxt);
  }

  public File downloadTxt(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Journal journal = createJournal(employeeId, year, month);
    final String journalTxt = journal.printJournalTxt();
    return journalRepository.saveToTxtFile(employeeId, journalTxt);
  }

  private Journal createJournal(final String employeeId, final Integer year, final Integer month) {
    final Employee employee = employeeRepository.findEmployeeByEmployeeId(employeeId);
    final List<Absence> absences = absenceRepository.findAbsencesByEmployeeId(employeeId, year, month);
    final List<RfidScan> allScans = rfidScanRepository.findAllRfIdScansByRfid(employee.getRfid(), year, month);
    return new Journal(employee, absences, allScans, year, month);
  }

  public List<String> getAllEmployeeNames() {
    return employeeRepository.findAll().stream()
      .map(Employee::getEmployeeId)
      .toList();
  }

  public File downloadBackup() {
    return null;
  }
}
