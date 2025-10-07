package de.zaehlermann.timetracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Correction {

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

  public String getEmployeeId() {
    return employeeId;
  }

  public LocalDate getWorkday() {
    return workday;
  }

  public LocalTime getLogin() {
    return login;
  }

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
}
