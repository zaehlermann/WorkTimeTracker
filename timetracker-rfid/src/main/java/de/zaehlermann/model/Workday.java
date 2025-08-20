package de.elementcamper.model;

import de.elementcamper.config.TimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Workday {

  public static final String HEADER_LINE = "DAY\t\t\tLOGIN\tLOGOUT\tHOURS\tSALDO";
  private final LocalDate day;
  private final LocalTime login;
  private final LocalTime logout;
  private final Duration hoursDay;
  private final BigDecimal saldo;

  public Workday(LocalDate day, LocalTime login, LocalTime logout) {
    this.day = day;
    this.login = login;
    this.logout = logout;
    this.hoursDay = Duration.between(login, logout);
    this.saldo = BigDecimal.valueOf((hoursDay.toMinutes() - (8 * 60))).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
  }

  public LocalDate getDay() {
    return day;
  }

  public String toTxtLine() {
    return day + "\t" +
        login.format(TimeFormat.TIME_FORMAT) + "\t" +
        logout.format(TimeFormat.TIME_FORMAT) + "\t" +
        String.format("%02d", hoursDay.toHoursPart()) + ":" + String.format("%02d", hoursDay.toMinutesPart()) + "\t" +
        saldo +
        System.lineSeparator();
  }

  public BigDecimal getSaldo() {
    return saldo;
  }
}
