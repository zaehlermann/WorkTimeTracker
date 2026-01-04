package de.zaehlermann.timetracker.i18n;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

  @Nonnull
  public static String get(@Nonnull final MessageKeys key, @Nonnull final Object... args) {
    return MessageFormat.format(get(key.key()), args);
  }

  @Nonnull
  public static String get(@Nonnull final String key, @Nonnull final Object... args) {
    return MessageFormat.format(get(key), args);
  }

  @Nonnull
  public static String get(@Nonnull final String key) {
    final ResourceBundle messages = ResourceBundle.getBundle(
        "de.zaehlermann.timetracker.i18n.messages",
        Locale.GERMAN);

    return messages.getString(key);
  }
}