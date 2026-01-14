package de.zaehlermann.timetracker.i18n;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class SupportedLocales {

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
