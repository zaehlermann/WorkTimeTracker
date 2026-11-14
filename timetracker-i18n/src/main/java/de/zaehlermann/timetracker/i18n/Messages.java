package de.zaehlermann.timetracker.i18n;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@NonNullByDefault
public class Messages {

  private static final String BASE_NAME = "de.zaehlermann.timetracker.i18n.messages";

  public static String get(final MessageKeys key, final Object... args) {
    return MessageFormat.format(get(key.key()), args);
  }

  public static String get(final String key, final Object... args) {
    return MessageFormat.format(get(key), args);
  }

  public static String get(final String key) {
    final ResourceBundle messages = ResourceBundle.getBundle(BASE_NAME, SupportedLocales.getDefault());
    return messages == null ? "MISSING_TRANSLATION" : messages.getString(key);
  }
}