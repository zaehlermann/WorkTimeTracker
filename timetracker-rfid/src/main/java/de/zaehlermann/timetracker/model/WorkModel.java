package de.zaehlermann.timetracker.model;

import de.zaehlermann.timetracker.i18n.MessageKeys;
import de.zaehlermann.timetracker.i18n.Messages;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorkModel implements AbstractCsvEntity<WorkModel> {

  public static final String HEADER_LINE = "EMPLOYEE_ID;VALID_FROM;VALID_UNTIL;WORKTIME_A_DAY_IN_MIN;BREAKTIME_A_DAY_IN_MIN";
  public static final WorkModel DEFAULT_WORKMODEL = new WorkModel("", LocalDate.of(2025, 1, 1), null, 8 * 60, 30);

  private final String employeeId;
  private final LocalDate validFrom;
  private final LocalDate validUntil; // null means unlimited
  public final int worktimeADayInMin;
  public final int breaktimeADayInMin;

  public WorkModel(@Nonnull final String employeeId,
                   @Nonnull final LocalDate validFrom,
                   @Nullable final LocalDate validUntil,
                   final int worktimeADayInMin,
                   final int breaktimeADayInMin) {

    this.employeeId = requireNonNull(employeeId);
    this.validFrom = requireNonNull(validFrom);
    this.validUntil = validUntil;
    this.worktimeADayInMin = worktimeADayInMin;
    this.breaktimeADayInMin = breaktimeADayInMin;
  }

  public int getExpectedPresenceTimeInMins() {
    return worktimeADayInMin + breaktimeADayInMin;
  }

  @Nonnull
  public String getEmployeeId() {
    return employeeId;
  }

  @Nonnull
  public LocalDate getValidFrom() {
    return validFrom;
  }

  @Nullable
  public LocalDate getValidUntil() {
    return validUntil;
  }

  public int getWorktimeADayInMin() {
    return worktimeADayInMin;
  }

  public int getBreaktimeADayInMin() {
    return breaktimeADayInMin;
  }

  @Override
  public String toCsvLine() {
    return employeeId + ";" +
           validFrom.toString() + ";" +
           (validUntil != null ? validUntil.toString() : "") + ";" +
           worktimeADayInMin + ";" +
           breaktimeADayInMin +
           System.lineSeparator();
  }

  public boolean isInEffectOnDay(@Nonnull final LocalDate day) {
    return validFrom.isEqual(day) || validFrom.isBefore(day) &&
                                     (validUntil == null || validUntil.isEqual(day) || validUntil.isAfter(day));
  }

  @Nonnull
  public static WorkModel fromCsvLine(@Nonnull final String csvLine) {
    final String[] split = csvLine.split(";", -1);
    final String employeeId = split[0];
    final LocalDate validFrom = LocalDate.parse(split[1]);
    final LocalDate validUntil = split[2].isBlank() ? null : LocalDate.parse(split[2]);
    final int worktimeADayInMin = Integer.parseInt(split[3]);
    final int breakTimeADayInMin = Integer.parseInt(split[4]);
    return new WorkModel(employeeId, validFrom, validUntil, worktimeADayInMin, breakTimeADayInMin);
  }

  @Override
  public boolean equals(final Object o) {
    if(!(o instanceof final WorkModel workModel)) return false;
    return Objects.equals(employeeId, workModel.employeeId) && Objects.equals(validFrom, workModel.validFrom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, validFrom);
  }

  @Nonnull
  public List<JournalSummaryItem> getSummaryItems() {
    return List.of(new JournalSummaryItem(Messages.get(MessageKeys.DAILY_WORKTIME), LocalTime.ofSecondOfDay(getWorktimeADayInMin() * 60L).toString() + "h "),
                   new JournalSummaryItem(Messages.get(MessageKeys.DAILY_BREAKTIME), LocalTime.ofSecondOfDay(getBreaktimeADayInMin() * 60L).toString() + "h"));
  }
}
