package de.zaehlermann.model;

public enum AbsenceType {

  /**
   * Krank
   */
  SICKNESS,
  /**
   * Urlaub
   */
  VACATION,
  /**
   * Entschuldigt abwesent, z.B. privater Termin mitten am Tag
   */
  ALLOWED_ABSENCE,
  /**
   * Feiertag
   */
  PUBLIC_HOLIDAY,
  /**
   * Betriebsferien
   */
  COMPANY_HOLIDAY;

}
