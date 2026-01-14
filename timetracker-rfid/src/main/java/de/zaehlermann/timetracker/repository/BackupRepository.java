package de.zaehlermann.timetracker.repository;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.annotation.Nonnull;

import de.zaehlermann.timetracker.globals.DefaultDirs;

public class BackupRepository {

  private BackupRepository() {}

  public static File backupRecordsDirectory() {
    final String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    final Path backupPath = Paths.get(DefaultDirs.BACKUP_BASE_DIR, "backup-" + now + ".zip");
    backupPath.getParent().toFile().mkdirs();
    backupRecordsDirectory(Paths.get(DefaultDirs.RECORDS_BASE_DIR), backupPath);
    return backupPath.toFile();
  }

  private static void backupRecordsDirectory(@Nonnull final Path sourceDir, @Nonnull final Path zipFile) {
    try(final ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile));
        final Stream<Path> walk = Files.walk(sourceDir)) {
      walk.filter(path -> !Files.isDirectory(path))
        .forEach(path -> {
          final ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
          try {
            zos.putNextEntry(zipEntry);
            Files.copy(path, zos);
            zos.closeEntry();
          }
          catch(final IOException e) {
            throw new UncheckedIOException(e);
          }
        });
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during zip path " + sourceDir + " to backup path" + zipFile, e);
    }
  }

}
