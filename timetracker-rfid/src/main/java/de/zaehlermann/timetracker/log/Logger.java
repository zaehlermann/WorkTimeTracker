package de.zaehlermann.timetracker.log;

public class Logger {

  private Logger() {}

  public static final Logger INSTANCE = new Logger();

  public void info(final String msg) {
    System.out.println(msg); // simple logging to console.
  }
}
