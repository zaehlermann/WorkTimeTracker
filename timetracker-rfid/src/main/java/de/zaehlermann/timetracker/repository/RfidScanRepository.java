package de.zaehlermann.timetracker.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import de.zaehlermann.timetracker.globals.Defaults;
import de.zaehlermann.timetracker.model.RfidScan;

public class RfidScanRepository {

  public RfidScanRepository() {
    new File(Defaults.BASE_DIR).mkdirs();
  }

  public String saveScan(final String input) throws IOException {
    final Path filePath = getTrackingFilePath(input);
    if(!filePath.toFile().exists()) {
      filePath.getParent().toFile().mkdirs();
      writeToFile(RfidScan.HEADER_LINE, filePath);
    }
    final String csvLine = new RfidScan(input).toCsvLine();
    writeToFile(csvLine, filePath);
    return csvLine;
  }

  private Path getTrackingFilePath(final String input) {
    return Path.of(Defaults.TRACKING_DIR, input + ".csv");
  }

  private void writeToFile(final String csvLine, final Path filePath) throws IOException {
    Files.writeString(filePath, csvLine, UTF_8,
                      StandardOpenOption.CREATE,
                      StandardOpenOption.APPEND);
  }

  public List<RfidScan> findAllRfIds(final String input) {
    final Path trackingFilePath = getTrackingFilePath(input);
    if(!trackingFilePath.toFile().exists()) {
      return Collections.emptyList();
    }

    try(Stream<String> stream = Files.lines(trackingFilePath, UTF_8)) {
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
}
