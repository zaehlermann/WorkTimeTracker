package de.zaehlermann.timetracker.service;

import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.repository.AbsenceRepository;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AbsenceService {

  private static final AbsenceRepository ABSENCE_REPOSITORY = new AbsenceRepository();

  @NonNull
  public List<Absence> findAll() {
    return ABSENCE_REPOSITORY.findAll();
  }

  @NonNull
  public List<Absence> delete(@NonNull final Set<Absence> selected) {
    final List<Absence> existing = ABSENCE_REPOSITORY.findAll();
    final List<Absence> toModified = new ArrayList<>(existing);
    toModified.removeAll(selected);
    ABSENCE_REPOSITORY.saveToFile(toModified);
    return toModified;
  }

  public void save(@NonNull final Absence newAbsence) {
    ABSENCE_REPOSITORY.appendToFile(newAbsence);
  }
}
