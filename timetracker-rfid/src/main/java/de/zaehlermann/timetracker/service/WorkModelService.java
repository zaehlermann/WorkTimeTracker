package de.zaehlermann.timetracker.service;

import de.zaehlermann.timetracker.model.WorkModel;
import de.zaehlermann.timetracker.repository.WorkModelRepository;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorkModelService {

  private static final WorkModelRepository WORK_MODEL_REPOSITORY = new WorkModelRepository();

  @NonNull
  public List<WorkModel> findAll() {
    return WORK_MODEL_REPOSITORY.findAll();
  }

  public void addWorkModel(@NonNull final WorkModel workModel) {
    WORK_MODEL_REPOSITORY.appendToFile(workModel);
  }

  @NonNull
  public List<WorkModel> deleteWorkModels(@NonNull final Set<WorkModel> workModels) {
    final List<WorkModel> existing = WORK_MODEL_REPOSITORY.findAll();
    final List<WorkModel> toModified = new ArrayList<>(existing);
    toModified.removeAll(workModels);
    WORK_MODEL_REPOSITORY.saveToFile(toModified);
    return toModified;
  }
}
