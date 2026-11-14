package de.zaehlermann.timetracker.service;

import de.zaehlermann.timetracker.model.Vacation;
import de.zaehlermann.timetracker.repository.VacationRepository;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VacationService {

  private static final VacationRepository VACATION_REPOSITORY = new VacationRepository();

  public void save(@NonNull final Vacation vacation) {
    VACATION_REPOSITORY.appendToFile(vacation);
  }

  @NonNull
  public List<Vacation> delete(@NonNull final Set<Vacation> selected) {
    final List<Vacation> existing = VACATION_REPOSITORY.findAll();
    final List<Vacation> toModified = new ArrayList<>(existing);
    toModified.removeAll(selected);
    VACATION_REPOSITORY.saveToFile(toModified);
    return toModified;
  }

  @NonNull
  public List<Vacation> findAll() {
    return VACATION_REPOSITORY.findAll();
  }
}
