package de.zaehlermann.timetracker.repository;

import java.io.File;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.globals.DefaultDirs;

public class JournalRepository extends AbstractCsvRepository {

  @Nonnull
  public File saveToTxtFile(@Nonnull final String employeeId,
                            @Nonnull final String selectedRange,
                            @Nonnull final String content) {
    final Path filePath = getJournalTxtFilePath(employeeId, selectedRange);
    saveToFile(content, filePath);
    return filePath.toFile();
  }

  @Nonnull
  public File saveToCsvFile(@Nonnull final String employeeId,
                            @Nonnull final String selectedRange,
                            @Nonnull final String content) {
    final Path filePath = getJournalCsvFilePath(employeeId, selectedRange);
    saveToFile(content, filePath);
    return filePath.toFile();
  }

  @Nonnull
  public static Path getJournalPdfFilePath(@Nonnull final String fileName, @Nonnull final String selectedRange) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + "-" + selectedRange + ".pdf");
  }

  @Nonnull
  private static Path getJournalTxtFilePath(@Nonnull final String fileName, @Nonnull final String selectedRange) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + "-" + selectedRange + ".txt");
  }

  @Nonnull
  private static Path getJournalCsvFilePath(@Nonnull final String fileName, @Nonnull final String selectedRange) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + "-" + selectedRange + ".csv");
  }

}
