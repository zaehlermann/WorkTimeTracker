package de.zaehlermann.timetracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AbsenceTypeTest {

  @Test
  void getSelectableValues() {
    assertEquals(AbsenceType.ALLOWED_ABSENCE, AbsenceType.getSelectableValues().getFirst());
  }
}