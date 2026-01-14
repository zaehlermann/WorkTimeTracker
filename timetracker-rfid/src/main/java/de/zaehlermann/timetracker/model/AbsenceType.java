package de.zaehlermann.timetracker.model;

import de.zaehlermann.timetracker.i18n.Messages;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.annotation.Nonnull;

public enum AbsenceType {

  /**
   * Wochenende
   */
  WEEKEND("absencetype.weekend","WE"),

  /**
   * Krank
   */
  SICKNESS("absencetype.sickness","S"),

  /**
   * Urlaub
   */
  VACATION("absencetype.vacation","V"),

  /**
   * Entschuldigt abwesend, z.B. privater Termin mitten am Tag
   */
  ALLOWED_ABSENCE("absencetype.allowed_absence","AE"),

  /**
   * Feiertag
   */
  PUBLIC_HOLIDAY("absencetype.public_holiday","PH"),

  /**
   * Betriebsferien
   */
  COMPANY_HOLIDAY("absencetype.company_holiday","CH"),

  /**
   * Gleitzeit
   */
  COMPENSATION_TIME("absencetype.compensation_time","CH");

  @Nonnull
  private final String messageKey;
  @Nonnull
  private final String printValueShort;

  AbsenceType(
    @Nonnull final String messageKey,
    @Nonnull final String printValueShort) {
    this.messageKey = messageKey;
    this.printValueShort = printValueShort;

  }

  @Nonnull
  public static List<AbsenceType> getSelectableValues() {
    final List<AbsenceType> list = new ArrayList<>(asList(SICKNESS, VACATION, ALLOWED_ABSENCE, PUBLIC_HOLIDAY, COMPANY_HOLIDAY));
    list.sort(Comparator.comparing(Enum::name));
    return list;
  }

  @Nonnull
  public String getPrintValueShort() {
    return printValueShort;
  }

  @Nonnull
  public String getPrintValueLong() {
    return Messages.get(this.messageKey);
  }

}
