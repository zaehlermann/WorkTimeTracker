package de.zaehlermann.timetracker.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.Vacation;

public class VacationRepository extends AbstractCsvRepository {

  public VacationRepository() {
    final Path filePath = getFilePath();
    if(!Files.exists(filePath)) {
      saveToFile(Vacation.HEADER_LINE + System.lineSeparator(), filePath);
    }
  }

  public void appendToFile(@Nonnull final Vacation vacation) {
    appendToFile(vacation.toCsvLine(), getFilePath());
  }

  public void saveToFile(@Nonnull final List<Vacation> vacations) {
    final Path filePath = getFilePath();
    saveToFile(Vacation.HEADER_LINE + System.lineSeparator(), filePath);
    vacations.forEach(this::appendToFile);
  }

  @Nonnull
  private Path getFilePath() {
    return Path.of(DefaultDirs.EMPLOYEE_DIR, "vacations.csv");
  }

  @Nonnull
  public List<Vacation> findVacationsByEmployeeId(@Nonnull final String employeeId) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Vacation.HEADER_LINE))
        .map(Vacation::fromCsvLine)
        .filter(e -> e.getEmployeeId().equals(employeeId))
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  @Nonnull
  public List<Vacation> findAll() {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Vacation.HEADER_LINE))
        .map(Vacation::fromCsvLine)
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }
}
