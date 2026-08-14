package de.zaehlermann.timetracker.i18n;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

public class SupportedLocales {
  private SupportedLocales() {
    /* This utility class should not be instantiated */
  }

  public static final Locale GERMAN = Locale.GERMAN;
  public static final Locale ENGLISH = Locale.ENGLISH;

  @Nonnull
  public static Locale getDefault() {
    return GERMAN;
  }

  @Nonnull
  public static List<Locale> getAll() {
    return List.of(GERMAN, ENGLISH);
  }
}
