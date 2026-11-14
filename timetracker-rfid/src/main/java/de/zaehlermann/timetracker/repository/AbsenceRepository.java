package de.zaehlermann.timetracker.repository;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.Absence;
import org.eclipse.jdt.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class AbsenceRepository extends AbstractCsvRepository {

  public AbsenceRepository() {
    final Path filePath = getFilePath();
    if(!Files.exists(filePath)) {
      saveToFile(Absence.HEADER_LINE + System.lineSeparator(), filePath);
    }
  }

  public void appendToFile(@NonNull final Absence absence) {
    appendToFile(absence.toCsvLine(), getFilePath());
  }

  public void saveToFile(@NonNull final List<Absence> absences) {
    final Path filePath = getFilePath();
    saveToFile(Absence.HEADER_LINE + System.lineSeparator(), filePath);
    absences.forEach(this::appendToFile);
  }

  @NonNull
  private Path getFilePath() {
    return Path.of(DefaultDirs.EMPLOYEE_DIR, "absence.csv");
  }

  @NonNull
  public List<Absence> findAbsencesByEmployeeId(@NonNull final String employeeId) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Absence.HEADER_LINE))
        .map(Absence::fromCsvLine)
        .filter(e -> employeeId.equals(e.employeeId()))
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  @NonNull
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
