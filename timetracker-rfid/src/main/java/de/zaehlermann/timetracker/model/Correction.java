package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Correction implements AbstractCsvEntity<Correction> {

  public static final String HEADER_LINE = "EMPLOYEE;WORKDAY;LOGIN;LOGOUT";
  private final String employeeId;
  private final LocalDate workday;
  private final LocalTime login;
  private final LocalTime logout;

  public Correction(@Nonnull final String employeeId, @Nonnull final LocalDate workday,
                    @Nullable final LocalTime login, @Nonnull final LocalTime logout) {
    this.employeeId = employeeId;
    this.workday = workday;
    this.login = login;
    this.logout = logout;
  }

  public Correction(@Nonnull final String employeeId, @Nonnull final String workday,
                    @Nullable final String login, @Nullable final String logout) {
    this.employeeId = employeeId;
    this.workday = LocalDate.parse(workday);
    this.login = login == null ? null : LocalTime.parse(login);
    this.logout = logout == null ? null : LocalTime.parse(logout);
  }

  @Nonnull
  public String getEmployeeId() {
    return employeeId;
  }

  @Nonnull
  public LocalDate getWorkday() {
    return workday;
  }

  @Nullable
  public LocalTime getLogin() {
    return login;
  }

  @Nullable
  public LocalTime getLogout() {
    return logout;
  }

  @Override
  public boolean equals(final Object o) {
    if(!(o instanceof final Correction that)) return false;
    return Objects.equals(employeeId, that.employeeId) && Objects.equals(workday, that.workday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, workday);
  }

  @Nonnull
  public String toCsvLine() {
    return employeeId + ";" + workday + ";" + login + ";" + logout + System.lineSeparator();
  }

  @Nonnull
  public static Correction fromCsvLine(final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    return new Correction(split[0], split[1], split[2], split[3]);
  }
}
