package de.zaehlermann.timetracker.repository;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.RfidScan;

public class RfidScanRepository extends AbstractCsvRepository {

  private final Clock clock;

  public RfidScanRepository(@Nonnull final Clock clock) {
    this.clock = clock;
    createDefaultDirs();
  }

  public RfidScanRepository() {
    this.clock = Clock.systemDefaultZone();
    createDefaultDirs();
  }

  private static void createDefaultDirs() {
    new File(DefaultDirs.RECORDS_BASE_DIR).mkdirs();
    new File(DefaultDirs.TRACKING_DIR).mkdirs();
  }

  @Nonnull
  public String saveScan(@Nonnull final String rfid) {
    final Path filePath = getTrackingFilePath(rfid);
    if(!filePath.toFile().exists()) {
      filePath.getParent().toFile().mkdirs();
      appendToFile(RfidScan.HEADER_LINE, filePath);
    }
    final String csvLine = new RfidScan(clock, rfid).toCsvLine();
    appendToFile(csvLine, filePath);
    return csvLine;
  }

  @Nonnull
  public List<RfidScan> findAllRfIdScansByRfid(@Nonnull final String rfid) {
    final Path trackingFilePath = getTrackingFilePath(rfid);
    if(!trackingFilePath.toFile().exists()) {
      return emptyList();
    }

    try(final Stream<String> stream = Files.lines(trackingFilePath, UTF_8)) {
      return stream
        .filter(s -> !s.startsWith("RFID"))// skip header line
        .map(RfidScan::fromCsvLine)
        .distinct()
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Nonnull
  private Path getTrackingFilePath(@Nonnull final String input) {
    return Path.of(DefaultDirs.TRACKING_DIR, input + ".csv");
  }

  @Nonnull
  public List<String> findAllRfids() {
    try(final Stream<Path> list = Files.list(Path.of(DefaultDirs.TRACKING_DIR))) {
      return list
        .filter(Files::isRegularFile)
        .map(path -> {
          final String name = path.getFileName().toString();
          final int dotIndex = name.lastIndexOf('.');
          return (dotIndex > 0) ? name.substring(0, dotIndex) : name;
        }).toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading all RFIDs", e);
    }
  }
}
