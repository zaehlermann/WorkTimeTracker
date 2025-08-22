package de.zaehlermann.timetracker.globals;

public final class DefaultDirs {

  private DefaultDirs() {}

  public static final String BASE_DIR = System.getProperty("user.home") + "/worktimetracker/records";
  public static final String TRACKING_DIR = BASE_DIR + "/tracking";
  public static final String JOURNAL_DIR = BASE_DIR + "/journal";
  public static final String EMPLOYEE_DIR = BASE_DIR + "/employee";
}
