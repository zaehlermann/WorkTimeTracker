package de.zaehlermann.timetracker.model;

import jakarta.annotation.Nonnull;

public interface AbstractCsvEntity {

  @Nonnull
  String toCsvLine();

}
