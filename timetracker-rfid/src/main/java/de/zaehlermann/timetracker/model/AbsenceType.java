package de.zaehlermann.timetracker.model;

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

  private final String display;

  AbsenceType(final String display) {
    this.display = display;
  }

  public String getDisplay() {
    return display;
  }
}
