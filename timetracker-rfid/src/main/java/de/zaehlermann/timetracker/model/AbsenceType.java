package de.zaehlermann.timetracker.model;

import java.util.Arrays;
import java.util.List;

public enum AbsenceType {

  /**
   * Wochenende
   */
  WEEKEND("WE"),

  /**
   * Krank
   */
  SICKNESS("S"),
  /**
   * Urlaub
   */
  VACATION("V"),
  /**
   * Entschuldigt abwesent, z.B. privater Termin mitten am Tag
   */
  ALLOWED_ABSENCE("AE"),
  /**
   * Feiertag
   */
  PUBLIC_HOLIDAY("PH"),
  /**
   * Betriebsferien
   */
  COMPANY_HOLIDAY("CH");

  private final String printValue;

  AbsenceType(final String printValue) {
    this.printValue = printValue;
  }

  public static List<AbsenceType> getSelectableValues() {
    return Arrays.asList(SICKNESS, VACATION, ALLOWED_ABSENCE, PUBLIC_HOLIDAY, COMPANY_HOLIDAY);
  }

  public String getPrintValue() {
    return printValue;
  }
}
