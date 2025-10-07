package de.zaehlermann.timetracker.model;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

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

  AbsenceType(@Nonnull final String printValue) {
    this.printValue = printValue;
  }

  @Nonnull
  public static List<AbsenceType> getSelectableValues() {
    final List<AbsenceType> list = new ArrayList<>(asList(SICKNESS, VACATION, ALLOWED_ABSENCE, PUBLIC_HOLIDAY, COMPANY_HOLIDAY));
    list.sort(Comparator.comparing(Enum::name));
    return list;
  }

  @Nonnull
  public String getPrintValue() {
    return printValue;
  }
}
