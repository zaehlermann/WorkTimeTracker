package de.zaehlermann.timetracker.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbsenceTest {

  @Test
  void testThatGetTimeRangeInDaysCalculates5days() {

    final Absence vacationWeek = new Absence("employeeId", AbsenceType.VACATION,
                                             LocalDate.of(2026, Month.AUGUST, 10),
                                             LocalDate.of(2026, Month.AUGUST, 14),
                                             null, null);

    assertEquals(5, vacationWeek.getTimeRangeInDays());
  }

  @Test
  void testThatGetTimeRangeInDaysCalculatesOneDay() {

    final Absence vacationWeek = new Absence("employeeId", AbsenceType.VACATION,
                                             LocalDate.of(2026, Month.AUGUST, 10),
                                             LocalDate.of(2026, Month.AUGUST, 10),
                                             null, null);
    assertEquals(1, vacationWeek.getTimeRangeInDays());
  }
}