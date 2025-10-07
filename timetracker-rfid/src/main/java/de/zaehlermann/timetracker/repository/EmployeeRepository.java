package de.zaehlermann.timetracker.repository;

import de.zaehlermann.timetracker.model.Employee;

public class EmployeeRepository {

  public Employee findEmployee(final String input) {
    return new Employee(input); //TODO find from config file
  }
}
