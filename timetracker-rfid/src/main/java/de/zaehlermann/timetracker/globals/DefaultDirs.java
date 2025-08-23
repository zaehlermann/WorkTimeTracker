package de.zaehlermann.timetracker.globals;

public final class DefaultDirs {

  private DefaultDirs() {}

  public static final String RECORDS_BASE_DIR = System.getProperty("user.home") + "/worktimetracker/records";
  public static final String TRACKING_DIR = RECORDS_BASE_DIR + "/tracking";
  public static final String JOURNAL_DIR = RECORDS_BASE_DIR + "/journal";
  public static final String EMPLOYEE_DIR = RECORDS_BASE_DIR + "/employee";
  public static final String BACKUP_BASE_DIR = System.getProperty("user.home") + "/worktimetracker/backups";
}
