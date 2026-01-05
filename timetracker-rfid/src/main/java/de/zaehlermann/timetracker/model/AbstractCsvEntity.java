package de.zaehlermann.timetracker.model;

import javax.annotation.Nonnull;

public interface AbstractCsvEntity<T> {

  @Nonnull
  String toCsvLine();

}
