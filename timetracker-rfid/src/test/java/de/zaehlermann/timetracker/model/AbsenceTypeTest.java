package de.zaehlermann.timetracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbsenceTypeTest {

  @Test
  void getSelectableValues() {
    assertEquals(AbsenceType.ALLOWED_ABSENCE, AbsenceType.getSelectableValues().getFirst());
  }
}