package de.zaehlermann.timetracker.service;

import de.zaehlermann.timetracker.model.Employee;
import de.zaehlermann.timetracker.repository.EmployeeRepository;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EmployeeService {

  private static final EmployeeRepository EMPLOYEE_REPOSITORY = new EmployeeRepository();

  @NonNull
  public List<Employee> findAll() {
    return EMPLOYEE_REPOSITORY.findAll();
  }

  public void addEmployee(@NonNull final Employee employee) {
    EMPLOYEE_REPOSITORY.appendToFile(employee);
  }

  @NonNull
  public List<Employee> deleteByRfid(@NonNull final Set<Employee> employees) {
    final List<Employee> existing = EMPLOYEE_REPOSITORY.findAll();
    final List<Employee> toModified = new ArrayList<>(existing);
    toModified.removeAll(employees);
    EMPLOYEE_REPOSITORY.saveToFile(toModified);
    return toModified;
  }
}
