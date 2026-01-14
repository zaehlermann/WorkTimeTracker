package de.zaehlermann.timetracker.i18n;

import jakarta.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {

  private static final String BASE_NAME = "de.zaehlermann.timetracker.i18n.messages";

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
    final ResourceBundle messages = ResourceBundle.getBundle(BASE_NAME, SupportedLocales.getDefault());
    return messages == null ? "MISSING_TRANSLATION" : messages.getString(key);
  }
}