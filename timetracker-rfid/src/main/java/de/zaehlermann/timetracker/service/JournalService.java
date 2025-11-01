package de.zaehlermann.timetracker.service;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.model.Correction;
import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.model.Journal;
import de.zaehlermann.timetracker.model.RfidScan;
import de.zaehlermann.timetracker.model.WorkModel;
import de.zaehlermann.timetracker.repository.AbsenceRepository;
import de.zaehlermann.timetracker.repository.BackupRepository;
import de.zaehlermann.timetracker.repository.CorrectionRepository;
import de.zaehlermann.timetracker.repository.EmployeeRepository;
import de.zaehlermann.timetracker.repository.JournalRepository;
import de.zaehlermann.timetracker.repository.RfidScanRepository;
import de.zaehlermann.timetracker.repository.WorkModelRepository;

public class JournalService {

  private static final RfidScanRepository RFID_SCAN_REPOSITORY = new RfidScanRepository();
  private static final JournalRepository JOURNAL_REPOSITORY = new JournalRepository();
  private static final EmployeeRepository EMPLOYEE_REPOSITORY = new EmployeeRepository();
  private static final AbsenceRepository ABSENCE_REPOSITORY = new AbsenceRepository();
  private static final CorrectionRepository CORRECTION_REPOSITORY = new CorrectionRepository();
  private static final WorkModelRepository WORK_MODEL_REPOSITORY = new WorkModelRepository();

  @Nonnull
  public String createAndSaveJournalTxt(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Journal journal = createJournal(employeeId, year, month);
    final String journalTxt = journal.printJournalTxt();
    JOURNAL_REPOSITORY.saveToTxtFile(employeeId, journalTxt);
    return journalTxt;
  }

  @Nonnull
  public File downloadCsv(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Journal journal = createJournal(employeeId, year, month);
    final String journalTxt = journal.printJournalCsv();
    return JOURNAL_REPOSITORY.saveToCsvFile(employeeId, journalTxt);
  }

  @Nonnull
  public File downloadTxt(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Journal journal = createJournal(employeeId, year, month);
    final String journalTxt = journal.printJournalTxt();
    return JOURNAL_REPOSITORY.saveToTxtFile(employeeId, journalTxt);
  }

  @Nonnull
  public Journal createJournal(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    final Employee employee = EMPLOYEE_REPOSITORY.findEmployeeByEmployeeId(employeeId);
    final List<WorkModel> workModels = WORK_MODEL_REPOSITORY.findAllWorkModelsByEmployeeId(employeeId, year, month);
    final List<Absence> absences = ABSENCE_REPOSITORY.findAbsencesByEmployeeId(employeeId, year, month);
    final List<Correction> corrections = CORRECTION_REPOSITORY.findCorrectionsByEmployeeId(employeeId, year, month);
    final List<RfidScan> allScans = RFID_SCAN_REPOSITORY.findAllRfIdScansByRfid(employee.getRfid(), year, month);
    return new Journal(employee, workModels, absences, corrections, allScans, year, month);
  }

  @Nonnull
  public List<String> getAllEmployeeNames() {
    return EMPLOYEE_REPOSITORY.findAll().stream()
      .map(Employee::getEmployeeId)
      .toList();
  }

  @Nonnull
  public File downloadBackup() {
    return BackupRepository.backupRecordsDirectory();
  }
}
