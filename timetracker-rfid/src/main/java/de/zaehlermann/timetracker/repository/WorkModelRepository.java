package de.zaehlermann.timetracker.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import jakarta.annotation.Nonnull;

import de.zaehlermann.timetracker.globals.DefaultDirs;
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
        .filter(WorkModelRepository::isNotHeader)
        .map(WorkModel::fromCsvLine)
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  private static boolean isNotHeader(final String line) {
    return !line.startsWith(WorkModel.HEADER_LINE.substring(0, 10));
  }

  @Nonnull
  public List<WorkModel> findAllWorkModelsByEmployeeId(@Nonnull final String employeeId) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      final List<WorkModel> savedModels = lines
        .filter(line -> !line.isEmpty())
        .filter(WorkModelRepository::isNotHeader)
        .map(WorkModel::fromCsvLine)
        .filter(e -> employeeId.equals(e.getEmployeeId()))
        .toList();
      return savedModels.isEmpty() ? List.of(WorkModel.DEFAULT_WORKMODEL) : savedModels;
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

}
