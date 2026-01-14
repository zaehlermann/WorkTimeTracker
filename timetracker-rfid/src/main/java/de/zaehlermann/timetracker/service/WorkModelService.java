package de.zaehlermann.timetracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Nonnull;

import de.zaehlermann.timetracker.model.WorkModel;
import de.zaehlermann.timetracker.repository.WorkModelRepository;

public class WorkModelService {

  private static final WorkModelRepository WORK_MODEL_REPOSITORY = new WorkModelRepository();

  @Nonnull
  public List<WorkModel> findAll() {
    return WORK_MODEL_REPOSITORY.findAll();
  }

  public void addWorkModel(@Nonnull final WorkModel workModel) {
    WORK_MODEL_REPOSITORY.appendToFile(workModel);
  }

  @Nonnull
  public List<WorkModel> deleteWorkModels(@Nonnull final Set<WorkModel> workModels) {
    final List<WorkModel> existing = WORK_MODEL_REPOSITORY.findAll();
    final List<WorkModel> toModified = new ArrayList<>(existing);
    toModified.removeAll(workModels);
    WORK_MODEL_REPOSITORY.saveToFile(toModified);
    return toModified;
  }
}
