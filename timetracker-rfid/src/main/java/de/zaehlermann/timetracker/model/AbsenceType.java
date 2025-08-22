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
  SICKNESS("K"),
  /**
   * Urlaub
   */
  VACATION("U"),
  /**
   * Entschuldigt abwesent, z.B. privater Termin mitten am Tag
   */
  ALLOWED_ABSENCE("EA"),
  /**
   * Feiertag
   */
  PUBLIC_HOLIDAY("F"),
  /**
   * Betriebsferien
   */
  COMPANY_HOLIDAY("BF");

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
