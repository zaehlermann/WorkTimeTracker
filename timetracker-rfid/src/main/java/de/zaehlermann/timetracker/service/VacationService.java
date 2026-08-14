package de.zaehlermann.timetracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.model.Vacation;
import de.zaehlermann.timetracker.repository.VacationRepository;

public class VacationService {

  private static final VacationRepository VACATION_REPOSITORY = new VacationRepository();

  public void save(@Nonnull final Vacation vacation) {
    VACATION_REPOSITORY.appendToFile(vacation);
  }

  @Nonnull
  public List<Vacation> delete(@Nonnull final Set<Vacation> selected) {
    final List<Vacation> existing = VACATION_REPOSITORY.findAll();
    final List<Vacation> toModified = new ArrayList<>(existing);
    toModified.removeAll(selected);
    VACATION_REPOSITORY.saveToFile(toModified);
    return toModified;
  }

  @Nonnull
  public List<Vacation> findAll() {
    return VACATION_REPOSITORY.findAll();
  }
}
