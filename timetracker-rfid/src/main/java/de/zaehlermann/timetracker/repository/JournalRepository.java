package de.zaehlermann.timetracker.repository;

import java.io.File;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.globals.DefaultDirs;

public class JournalRepository extends AbstractCsvRepository {

  @Nonnull
  public File saveToTxtFile(@Nonnull final String employeeId, @Nonnull final String content) {
    final Path filePath = getJournalTxtFilePath(employeeId);
    saveToFile(content, filePath);
    return filePath.toFile();
  }

  @Nonnull
  public File saveToCsvFile(@Nonnull final String employeeId, @Nonnull final String content) {
    final Path filePath = getJournalXsvFilePath(employeeId);
    saveToFile(content, filePath);
    return filePath.toFile();
  }

  @Nonnull
  private Path getJournalTxtFilePath(@Nonnull final String fileName) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + ".txt");
  }

  @Nonnull
  private Path getJournalXsvFilePath(@Nonnull final String fileName) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + ".csv");
  }

}
