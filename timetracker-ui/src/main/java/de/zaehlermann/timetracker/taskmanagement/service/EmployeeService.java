package de.zaehlermann.timetracker.taskmanagement.service;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements Serializable {
  @Serial
  private static final long serialVersionUID = 4242379703790122434L;

  public List<String> getAllEmployeeNames() {
    return List.of("t1", "t2", "t3", "t4");
  }
}
