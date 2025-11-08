package de.zaehlermann.timetracker.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class AbstractCsvRepository {

  public static void saveToFile(final String fileContent, final Path filePath) {
    filePath.getParent().toFile().mkdirs();
    try {
      Files.writeString(filePath, fileContent, UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during writing file content to path: " + filePath, e);
    }
  }

  public void appendToFile(final String fileContent, final Path filePath) {
    try {
      Files.writeString(filePath, fileContent, UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during appending file content to path: " + filePath, e);
    }
  }

}
