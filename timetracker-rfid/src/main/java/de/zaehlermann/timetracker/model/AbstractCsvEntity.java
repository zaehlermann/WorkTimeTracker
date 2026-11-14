package de.zaehlermann.timetracker.model;

import org.eclipse.jdt.annotation.NonNull;

public interface AbstractCsvEntity<T> {

  @NonNull
  String toCsvLine();

}
