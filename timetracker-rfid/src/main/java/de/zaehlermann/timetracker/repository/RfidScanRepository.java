package de.zaehlermann.timetracker.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.RfidScan;

public class RfidScanRepository extends AbstractCsvRepository {

  private Clock clock;

  public RfidScanRepository(final Clock clock) {
    this.clock = clock;
    new File(DefaultDirs.BASE_DIR).mkdirs();
  }

  public RfidScanRepository() {
    new File(DefaultDirs.BASE_DIR).mkdirs();
  }

  public String saveScan(final String rfid) {
    final Path filePath = getTrackingFilePath(rfid);
    if(!filePath.toFile().exists()) {
      filePath.getParent().toFile().mkdirs();
      appendToFile(RfidScan.HEADER_LINE, filePath);
    }
    final String csvLine = new RfidScan(clock, rfid).toCsvLine();
    appendToFile(csvLine, filePath);
    return csvLine;
  }

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

  public List<RfidScan> findAllRfIdScansByRfid(final String rfid, final Integer year, final Integer month) {
    final Path trackingFilePath = getTrackingFilePath(rfid);
    if(!trackingFilePath.toFile().exists()) {
      return Collections.emptyList();
    }

    try(Stream<String> stream = Files.lines(trackingFilePath, UTF_8)) {
      return stream
        .filter(s -> !s.startsWith("RFID"))// skip header line
        .map(RfidScan::fromCsvLine)
        .filter(scan -> (year == null || scan.getWorkday().getYear() == year) &&
                        (month == null || scan.getWorkday().getMonthValue() == month))
        .distinct()
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private Path getTrackingFilePath(final String input) {
    return Path.of(DefaultDirs.TRACKING_DIR, input + ".csv");
  }

}
