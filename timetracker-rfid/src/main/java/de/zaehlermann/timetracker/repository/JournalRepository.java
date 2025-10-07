package de.zaehlermann.timetracker.repository;

import java.nio.file.Path;

import de.zaehlermann.timetracker.globals.DefaultDirs;

public class JournalRepository extends AbstractCsvRepository {

  public void saveToFile(final String rfid, final String content) {
    final Path filePath = getJournalFilePath(rfid);
    saveToFile(content, filePath);
  }

  private Path getJournalFilePath(final String rfid) {
    return Path.of(DefaultDirs.JOURNAL_DIR, rfid + ".txt");
  }

}
