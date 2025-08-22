package de.zaehlermann.timetracker.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import de.zaehlermann.timetracker.globals.Defaults;
import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.model.Employee;

public class AbsenceRepository extends AbstractCsvRepository {

  public AbsenceRepository() {
    final Path filePath = getFilePath();
    if(!Files.exists(filePath)) {
      saveToFile(Absence.HEADER_LINE + System.lineSeparator(), filePath);
    }
  }

  public void appendToFile(final Absence absence) {
    appendToFile(absence.toCsvLine(), getFilePath());
  }

  public void saveToFile(final List<Absence> absences) {
    final Path filePath = getFilePath();
    saveToFile(Absence.HEADER_LINE + System.lineSeparator(), filePath);
    absences.forEach(this::appendToFile);
  }

  private Path getFilePath() {
    return Path.of(Defaults.EMPLOYEE_DIR, "absence.csv");
  }

  public Absence findEmployeeByEmployeeId(final String employeeId) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .map(Absence::fromCsvLine)
        .filter(e -> employeeId.equals(e.employeeId()))
        .findFirst()
        .orElse(null);
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  public List<Absence> findAll() {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Absence.HEADER_LINE))
        .map(Absence::fromCsvLine)
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

}
