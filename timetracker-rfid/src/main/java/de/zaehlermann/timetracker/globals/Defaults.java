package de.zaehlermann.timetracker.globals;

public final class Defaults {

  private Defaults() {}

  public static final String BASE_DIR = System.getProperty("user.dir") + "/records";
  public static final String TRACKING_DIR = BASE_DIR + "/tracking";
  public static final String JOURNAL_DIR = BASE_DIR + "/journal";
}
