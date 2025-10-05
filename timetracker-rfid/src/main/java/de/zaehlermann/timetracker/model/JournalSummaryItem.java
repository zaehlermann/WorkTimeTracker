package de.zaehlermann.timetracker.model;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * A simple key-value pair used in journal summaries.
 */
public class JournalSummaryItem {

  public final String key;
  public final String value;

  public JournalSummaryItem(@Nonnull final String key, @Nonnull final String value) {
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

  @Nonnull
  public String getKey() {
    return key;
  }

  @Nonnull
  public String getValue() {
    return value;
  }
}
