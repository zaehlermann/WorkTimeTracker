package de.zaehlermann.timetracker.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.model.WorkModel;

public class WorkModelRepository extends AbstractCsvRepository {

  public WorkModelRepository() {
    final Path filePath = getFilePath();
    if(!Files.exists(filePath)) {
      saveToFile(WorkModel.HEADER_LINE + System.lineSeparator(), filePath);
    }
  }

  public void appendToFile(@Nonnull final WorkModel workModel) {
    appendToFile(workModel.toCsvLine(), getFilePath());
  }

  public void saveToFile(@Nonnull final List<WorkModel> workModels) {
    final Path filePath = getFilePath();
    saveToFile(WorkModel.HEADER_LINE + System.lineSeparator(), filePath);
    workModels.forEach(this::appendToFile);
  }

  @Nonnull
  private Path getFilePath() {
    return Path.of(DefaultDirs.EMPLOYEE_DIR, "work-models.csv");
  }

  @Nonnull
  public List<WorkModel> findAll() {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(WorkModel.HEADER_LINE))
        .map(WorkModel::fromCsvLine)
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  @Nonnull
  public List<WorkModel> findAllWorkModelsByEmployeeId(@Nonnull final String employeeId, @Nullable final Integer year, @Nonnull final Integer month) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(WorkModel.HEADER_LINE))
        .map(WorkModel::fromCsvLine)
        .filter(e -> employeeId.equals(e.getEmployeeId()))
        .filter(e -> e.isInMonth(year, month))
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

}
