package de.zaehlermann.timetracker.repository;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import org.eclipse.jdt.annotation.NonNull;

import java.io.File;
import java.nio.file.Path;

public class JournalRepository extends AbstractCsvRepository {

  @NonNull
  public File saveToTxtFile(@NonNull final String employeeId,
                            @NonNull final String selectedRange,
                            @NonNull final String content) {
    final Path filePath = getJournalTxtFilePath(employeeId, selectedRange);
    saveToFile(content, filePath);
    return filePath.toFile();
  }

  @NonNull
  public File saveToCsvFile(@NonNull final String employeeId,
                            @NonNull final String selectedRange,
                            @NonNull final String content) {
    final Path filePath = getJournalCsvFilePath(employeeId, selectedRange);
    saveToFile(content, filePath);
    return filePath.toFile();
  }

  @NonNull
  public static Path getJournalPdfFilePath(@NonNull final String fileName, @NonNull final String selectedRange) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + "-" + selectedRange + ".pdf");
  }

  @NonNull
  private static Path getJournalTxtFilePath(@NonNull final String fileName, @NonNull final String selectedRange) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + "-" + selectedRange + ".txt");
  }

  @NonNull
  private static Path getJournalCsvFilePath(@NonNull final String fileName, @NonNull final String selectedRange) {
    return Path.of(DefaultDirs.JOURNAL_DIR, fileName + "-" + selectedRange + ".csv");
  }

}
