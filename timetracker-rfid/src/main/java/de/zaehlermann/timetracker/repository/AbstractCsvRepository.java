package de.zaehlermann.timetracker.repository;

import org.eclipse.jdt.annotation.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AbstractCsvRepository {

  public static void saveToFile(@NonNull final String fileContent, @NonNull final Path filePath) {
    filePath.getParent().toFile().mkdirs();
    try {
      Files.writeString(filePath, fileContent, UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
    } catch (final IOException e) {
      throw new IllegalStateException("Error during writing file content to path: " + filePath, e);
    }
  }

  public void appendToFile(@NonNull final String fileContent, @NonNull final Path filePath) {
    try {
      Files.writeString(filePath, fileContent, UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    } catch (final IOException e) {
      throw new IllegalStateException("Error during appending file content to path: " + filePath, e);
    }
  }

}
