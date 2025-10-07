package de.zaehlermann.timetracker.model;

public interface AbstractCsvEntity<T> {

  String toCsvLine();

}
