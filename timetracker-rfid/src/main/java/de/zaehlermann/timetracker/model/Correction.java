package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

public class Correction implements AbstractCsvEntity {

  public static final String HEADER_LINE = "EMPLOYEE;WORKDAY;LOGIN;LOGOUT";
  private final String employeeId;
  private final LocalDate workday;
  private final LocalTime login;
  private final LocalTime logout;

  public Correction(@Nonnull final String employeeId,
                    @Nonnull final LocalDate workday,
                    @Nullable final LocalTime login,
                    @Nonnull final LocalTime logout) {
    this.employeeId = employeeId;
    this.workday = workday;
    this.login = login;
    this.logout = logout;
  }

  public Correction(@Nonnull final String employeeId, @Nonnull final String workday,
                    @Nullable final String login, @Nullable final String logout) {
    this.employeeId = employeeId;
    this.workday = LocalDate.parse(workday);
    this.login = StringUtils.isBlank(login) ? null : LocalTime.parse(login);
    this.logout = StringUtils.isBlank(logout) ? null : LocalTime.parse(logout);
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
    return employeeId + ";" +
           workday + ";" +
           (login == null ? "" : login) + ";" +
           (logout == null ? "" : logout) + System.lineSeparator();
  }

  @Nonnull
  public static Correction fromCsvLine(@Nonnull final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    return new Correction(split[0], split[1], split[2], split[3]);
  }
}
