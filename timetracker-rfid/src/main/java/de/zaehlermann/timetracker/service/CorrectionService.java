package de.zaehlermann.timetracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.model.Correction;
import de.zaehlermann.timetracker.repository.CorrectionRepository;

public class CorrectionService {

  private static final CorrectionRepository CORRECTION_REPOSITORY = new CorrectionRepository();

  public void save(@Nonnull final Correction correction) {
    CORRECTION_REPOSITORY.appendToFile(correction);
  }

  @Nonnull
  public List<Correction> delete(@Nonnull final Set<Correction> selected) {
    final List<Correction> existing = CORRECTION_REPOSITORY.findAll();
    final List<Correction> toModified = new ArrayList<>(existing);
    toModified.removeAll(selected);
    CORRECTION_REPOSITORY.saveToFile(toModified);
    return toModified;
  }

  @Nonnull
  public List<Correction> findAll() {
    return CORRECTION_REPOSITORY.findAll();
  }
}
