package de.zaehlermann.timetracker.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.annotation.Nonnull;

import de.zaehlermann.timetracker.model.Correction;

public class CorrectionRepository {

  @Nonnull
  public List<Correction> findCorrectionsByEmployeeId(@Nonnull final String employeeId, @Nonnull final Integer year, @Nonnull final Integer month) {
    //TODO create UI and persist
    return List.of(new Correction("m1", LocalDate.of(2025, 8, 24), null, LocalTime.of(15, 0)),
                   new Correction("m1", LocalDate.now(), LocalTime.of(6, 0), LocalTime.of(15, 0)));
  }
}
