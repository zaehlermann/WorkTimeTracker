package de.zaehlermann.timetracker.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import de.zaehlermann.timetracker.globals.Defaults;

public class JournalRepository {

  public void writeNewFile(final String rfid, final String content) throws IOException {
    final Path filePath = getJournalFilePath(rfid);
    filePath.getParent().toFile().mkdirs();
    Files.writeString(filePath, content, UTF_8,
                      StandardOpenOption.CREATE,
                      StandardOpenOption.TRUNCATE_EXISTING);
  }

  private Path getJournalFilePath(final String rfid) {
    return Path.of(Defaults.JOURNAL_DIR, rfid + ".txt");
  }

}
