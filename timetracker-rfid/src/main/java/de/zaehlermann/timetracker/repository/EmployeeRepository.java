package de.zaehlermann.timetracker.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import de.zaehlermann.timetracker.globals.DefaultDirs;
import de.zaehlermann.timetracker.model.Employee;

public class EmployeeRepository extends AbstractCsvRepository {

  public static final String UNKNOWN = "Unknown";

  public EmployeeRepository() {
    final Path filePath = getFilePath();
    if(!Files.exists(filePath)) {
      saveToFile(Employee.HEADER_LINE + System.lineSeparator(), filePath);
    }
  }

  public void appendToFile(final Employee employee) {
    appendToFile(employee.toCsvLine(), getFilePath());
  }

  public void saveToFile(final List<Employee> employees) {
    final Path filePath = getFilePath();
    saveToFile(Employee.HEADER_LINE + System.lineSeparator(), filePath);
    employees.forEach(this::appendToFile);
  }

  private Path getFilePath() {
    return Path.of(DefaultDirs.EMPLOYEE_DIR, "employees.csv");
  }

  public Employee findEmployeeByEmployeeId(final String employeeId) {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .map(Employee::fromCsvLine)
        .filter(e -> employeeId.equals(e.getEmployeeId()))
        .findFirst()
        .orElse(new Employee(employeeId, UNKNOWN, UNKNOWN, UNKNOWN));
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

  public List<Employee> findAll() {
    final Path filePath = getFilePath();
    try(final Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
      return lines
        .filter(line -> !line.isEmpty())
        .filter(line -> !line.equals(Employee.HEADER_LINE))
        .map(Employee::fromCsvLine)
        .toList();
    }
    catch(final IOException e) {
      throw new IllegalStateException("Error during reading file from path " + filePath, e);
    }
  }

}
