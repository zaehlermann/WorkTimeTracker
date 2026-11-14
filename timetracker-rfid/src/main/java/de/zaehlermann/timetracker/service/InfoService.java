package de.zaehlermann.timetracker.service;

import org.eclipse.jdt.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public class InfoService {

  private String appVersion;

  @NonNull
  public String getAppVersion() {
    if(appVersion == null) {
      appVersion = readAppVersion();
    }
    return appVersion;
  }

  @NonNull
  private String readAppVersion() {
    try(final InputStream resourceAsStream = getClass().getResourceAsStream("version.txt")) {
      requireNonNull(resourceAsStream, "version file not found");
      return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
    }
    catch(final IOException e) {
      throw new IllegalStateException("Exception during reading app version", e);
    }
  }
}
