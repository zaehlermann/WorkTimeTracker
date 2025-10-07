package de.elementcamper.repository;

import de.elementcamper.model.Employee;
import de.elementcamper.model.Journal;
import de.elementcamper.model.RfIdScan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RfidScanRepository {

  private static final String TRACKING_FILE_PATH = "tracking";
  private static final String JOURNAL_FILE_PATH = "journal";
  private final String baseDir;

  public RfidScanRepository(String baseDir) {
    this.baseDir = baseDir;
    new File(baseDir).mkdirs();
  }

  public void saveScan(String input) throws IOException {
    Path filePath = getTrackingFilePath(input);
    if (!filePath.toFile().exists()) {
      filePath.getParent().toFile().mkdirs();
      writeToFile(RfIdScan.HEADER_LINE, filePath);
    }
    writeToFile(new RfIdScan(input).toCsvLine(), filePath);
  }

  private Path getTrackingFilePath(String input) {
    return Path.of(baseDir, TRACKING_FILE_PATH, input + ".csv");
  }

  private Path getJournalFilePath(String input) {
    return Path.of(baseDir, JOURNAL_FILE_PATH, input + ".txt");
  }

  private void writeToFile(String csvLine, Path filePath) throws IOException {
    System.out.println(csvLine);
    Files.writeString(filePath, csvLine, UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
  }

  public void createJournal(String input) throws IOException {
    Path rfIdScanFilePath = getTrackingFilePath(input);
    Path journalFilePath = getJournalFilePath(input);
    List<RfIdScan> allScans = findAllRfIds(rfIdScanFilePath);
    Employee employee = findEmployee(input);
    String journal = new Journal(employee, allScans).printJournal();
    System.out.println(journal);
    writeNewFile(journalFilePath, journal);
  }

  private void writeNewFile(Path filePath, String content) throws IOException {
    filePath.getParent().toFile().mkdirs();
    Files.writeString(filePath, content, UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  private Employee findEmployee(String input) {
    return new Employee(input); //TODO find from config file
  }

  private List<RfIdScan> findAllRfIds(Path filePath) {
    try (Stream<String> stream = Files.lines(filePath, UTF_8)) {
      return stream
          .filter(s-> !s.startsWith("RFID"))// skip header line
          .map(RfIdScan::fromCsvLine)
          .distinct()
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
