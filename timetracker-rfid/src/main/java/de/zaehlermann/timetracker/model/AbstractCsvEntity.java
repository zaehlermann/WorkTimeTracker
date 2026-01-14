package de.zaehlermann.timetracker.model;

import jakarta.annotation.Nonnull;

public interface AbstractCsvEntity<T> {

  @Nonnull
  String toCsvLine();

}
