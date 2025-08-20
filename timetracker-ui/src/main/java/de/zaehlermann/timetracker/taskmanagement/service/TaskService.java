package de.zaehlermann.timetracker.taskmanagement.service;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.zaehlermann.timetracker.taskmanagement.domain.Task;
import de.zaehlermann.timetracker.taskmanagement.domain.TaskRepository;

@Service
@PreAuthorize("isAuthenticated()")
public class TaskService implements Serializable {

  @Serial
  private static final long serialVersionUID = 2931377461536510568L;
  private final TaskRepository taskRepository;

  private final Clock clock;

  TaskService(final TaskRepository taskRepository, final Clock clock) {
    this.taskRepository = taskRepository;
    this.clock = clock;
  }

  @Transactional
  public void createTask(final String description, @Nullable final LocalDate dueDate) {
    if("fail".equals(description)) {
      throw new RuntimeException("This is for testing the error handler");
    }
    final Task task = new Task();
    task.setDescription(description);
    task.setCreationDate(clock.instant());
    task.setDueDate(dueDate);
    taskRepository.saveAndFlush(task);
  }

  @Transactional(readOnly = true)
  public List<Task> list(final Pageable pageable) {
    return taskRepository.findAllBy(pageable).toList();
  }

}
