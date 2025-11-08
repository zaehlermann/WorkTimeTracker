package de.zaehlermann.timetracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.repository.EmployeeRepository;

public class EmployeeService {

  private static final EmployeeRepository EMPLOYEE_REPOSITORY = new EmployeeRepository();

  @Nonnull
  public List<Employee> findAll() {
    return EMPLOYEE_REPOSITORY.findAll();
  }

  public void addEmployee(@Nonnull final Employee employee) {
    EMPLOYEE_REPOSITORY.appendToFile(employee);
  }

  @Nonnull
  public List<Employee> deleteByRfid(@Nonnull final Set<Employee> employees) {
    final List<Employee> existing = EMPLOYEE_REPOSITORY.findAll();
    final List<Employee> toModified = new ArrayList<>(existing);
    toModified.removeAll(employees);
    EMPLOYEE_REPOSITORY.saveToFile(toModified);
    return toModified;
  }
}
