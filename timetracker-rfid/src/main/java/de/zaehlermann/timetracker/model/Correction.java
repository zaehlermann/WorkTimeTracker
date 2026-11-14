package de.zaehlermann.timetracker.model;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Correction implements AbstractCsvEntity<Correction> {

  public static final String HEADER_LINE = "EMPLOYEE;WORKDAY;LOGIN;LOGOUT";
  private final String employeeId;
  private final LocalDate workday;
  private final LocalTime login;
  private final LocalTime logout;

  public Correction(@NonNull final String employeeId,
                    @NonNull final LocalDate workday,
                    @Nullable final LocalTime login,
                    @NonNull final LocalTime logout) {
    this.employeeId = employeeId;
    this.workday = workday;
    this.login = login;
    this.logout = logout;
  }

  public Correction(@NonNull final String employeeId,
                    @NonNull final String workday,
                    @Nullable final String login,
                    @Nullable final String logout) {
    this.employeeId = employeeId;
    this.workday = LocalDate.parse(workday);
    this.login = StringUtils.isBlank(login) ? null : LocalTime.parse(login);
    this.logout = StringUtils.isBlank(logout) ? null : LocalTime.parse(logout);
  }

  @NonNull
  public String getEmployeeId() {
    return employeeId;
  }

  @NonNull
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

  @NonNull
  public String toCsvLine() {
    return employeeId + ";" +
           workday + ";" +
           (login == null ? "" : login) + ";" +
           (logout == null ? "" : logout) + System.lineSeparator();
  }

  @NonNull
  public static Correction fromCsvLine(@NonNull final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    return new Correction(split[0], split[1], split[2], split[3]);
  }
}
