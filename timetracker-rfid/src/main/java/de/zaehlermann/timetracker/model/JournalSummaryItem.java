package de.zaehlermann.timetracker.model;

import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A simple key-value pair used in journal summaries.
 */
public class JournalSummaryItem {

  public final String key;
  public final String value;

  public JournalSummaryItem(@NonNull final String key, @NonNull final String value) {
    this.key = requireNonNull(key);
    this.value = requireNonNull(value);
  }

  @Override
  public boolean equals(final Object o) {
    if(!(o instanceof final JournalSummaryItem that)) return false;
    return Objects.equals(key, that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key);
  }

  @NonNull
  public String getKey() {
    return key;
  }

  @NonNull
  public String getValue() {
    return value;
  }

  @NonNull
  public String toTxtLine() {
    return key + ": " + value + System.lineSeparator();
  }
  @NonNull
  public String toCsvLine() {
    return key + ";" + value + System.lineSeparator();
  }
}
