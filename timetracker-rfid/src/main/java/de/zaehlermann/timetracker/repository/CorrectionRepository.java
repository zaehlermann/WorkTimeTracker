package de.zaehlermann.timetracker.repository;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.Correction;
import org.eclipse.jdt.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class CorrectionRepository extends AbstractCsvRepository {

  public CorrectionRepository() {
    final Path filePath = getFilePath();
    if(!Files.exists(filePath)) {
      saveToFile(Correction.HEADER_LINE + System.lineSeparator(), filePath);
    }
  }

  public void appendToFile(@NonNull final Correction correction) {
    appendToFile(correction.toCsvLine(), getFilePath());
  }

  public void saveToFile(@NonNull final List<Correction> corrections) {
    final Path filePath = getFilePath();
    saveToFile(Correction.HEADER_LINE + System.lineSeparator(), filePath);
    corrections.forEach(this::appendToFile);
  }

  @NonNull
  private Path getFilePath() {
    return Path.of(DefaultDirs.EMPLOYEE_DIR, "corrections.csv");
  }

  @NonNull
  public List<Correction> findCorrectionsByEmployeeId(@NonNull final String employeeId) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Correction.HEADER_LINE))
        .map(Correction::fromCsvLine)
        .filter(e -> e.getEmployeeId().equals(employeeId))
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  @NonNull
  public List<Correction> findAll() {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Correction.HEADER_LINE))
        .map(Correction::fromCsvLine)
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }
}
