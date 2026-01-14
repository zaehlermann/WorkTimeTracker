package de.zaehlermann.timetracker.service;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.annotation.Nonnull;

public class InfoService {

  private String appVersion;

  @Nonnull
  public String getAppVersion() {
    if(appVersion == null) {
      appVersion = readAppVersion();
    }
    return appVersion;
  }

  @Nonnull
  private String readAppVersion() {
    try(InputStream resourceAsStream = getClass().getResourceAsStream("version.txt")) {
      requireNonNull(resourceAsStream, "version file not found");
      return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
    }
    catch(final IOException e) {
      throw new IllegalStateException("Exception during reading app version", e);
    }
  }
}
