package de.zaehlermann.repository;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

import de.zaehlermann.model.Employee;
import de.zaehlermann.model.Journal;
import de.zaehlermann.model.RfIdScan;

public class RfidScanRepository {

  private static final String TRACKING_FILE_PATH = "tracking";
  private static final String JOURNAL_FILE_PATH = "journal";
  private final String baseDir;

  public RfidScanRepository(final String baseDir) {
    this.baseDir = baseDir;
    new File(baseDir).mkdirs();
  }

  public void saveScan(final String input) throws IOException {
    final Path filePath = getTrackingFilePath(input);
    if (!filePath.toFile().exists()) {
      filePath.getParent().toFile().mkdirs();
      writeToFile(RfIdScan.HEADER_LINE, filePath);
    }
    writeToFile(new RfIdScan(input).toCsvLine(), filePath);
  }

  private Path getTrackingFilePath(final String input) {
    return Path.of(baseDir, TRACKING_FILE_PATH, input + ".csv");
  }

  private Path getJournalFilePath(final String input) {
    return Path.of(baseDir, JOURNAL_FILE_PATH, input + ".txt");
  }

  private void writeToFile(final String csvLine, final Path filePath) throws IOException {
    System.out.println(csvLine);
    Files.writeString(filePath, csvLine, UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
  }

  public void createJournal(final String input) throws IOException {
    final Path rfIdScanFilePath = getTrackingFilePath(input);
    final Path journalFilePath = getJournalFilePath(input);
    final List<RfIdScan> allScans = findAllRfIds(rfIdScanFilePath);
    final Employee employee = findEmployee(input);
    final String journal = new Journal(employee, allScans).printJournal();
    System.out.println(journal);
    writeNewFile(journalFilePath, journal);
  }

  private void writeNewFile(final Path filePath, final String content) throws IOException {
    filePath.getParent().toFile().mkdirs();
    Files.writeString(filePath, content, UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  private Employee findEmployee(final String input) {
    return new Employee(input); //TODO find from config file
  }

  private List<RfIdScan> findAllRfIds(final Path filePath) {
    try (Stream<String> stream = Files.lines(filePath, UTF_8)) {
      return stream
          .filter(s-> !s.startsWith("RFID"))// skip header line
          .map(RfIdScan::fromCsvLine)
          .distinct()
          .collect(toList());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
