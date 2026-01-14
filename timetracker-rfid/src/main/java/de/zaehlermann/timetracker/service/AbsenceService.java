package de.zaehlermann.timetracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Nonnull;

import de.zaehlermann.timetracker.model.Absence;
import de.zaehlermann.timetracker.repository.AbsenceRepository;

public class AbsenceService {

  private static final AbsenceRepository ABSENCE_REPOSITORY = new AbsenceRepository();

  @Nonnull
  public List<Absence> findAll() {
    return ABSENCE_REPOSITORY.findAll();
  }

  @Nonnull
  public List<Absence> delete(@Nonnull final Set<Absence> selected) {
    final List<Absence> existing = ABSENCE_REPOSITORY.findAll();
    final List<Absence> toModified = new ArrayList<>(existing);
    toModified.removeAll(selected);
    ABSENCE_REPOSITORY.saveToFile(toModified);
    return toModified;
  }

  public void save(@Nonnull final Absence newAbsence) {
    ABSENCE_REPOSITORY.appendToFile(newAbsence);
  }
}
