package de.zaehlermann.timetracker.i18n;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.util.List;
import java.util.Locale;

@NonNullByDefault
public class SupportedLocales {
  private SupportedLocales() {
    /* This utility class should not be instantiated */
  }

  public static final Locale GERMAN = Locale.GERMAN;
  public static final Locale ENGLISH = Locale.ENGLISH;

  public static Locale getDefault() {
    return GERMAN;
  }

  public static List<Locale> getAll() {
    return List.of(GERMAN, ENGLISH);
  }
}
