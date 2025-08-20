package de.zaehlermann.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RfIdScan {

  public static final String HEADER_LINE = "RFID;DAY;TIME" + System.lineSeparator();
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
  private final String rfid;
  private final LocalDate workday;
  private final LocalTime scanTime;

  public RfIdScan(String rfid) {
    this.rfid = rfid;
    this.workday = LocalDate.now();
    this.scanTime = LocalTime.now();
  }

  public RfIdScan(String rfid, String workday, String scanTime) {
    this.rfid = rfid;
    this.workday = LocalDate.parse(workday);
    this.scanTime = LocalTime.parse(scanTime);
  }

  public String getRfid() {
    return rfid;
  }

  public LocalDate getWorkday() {
    return workday;
  }

  public LocalTime getScanTime() {
    return scanTime;
  }

  public static RfIdScan fromCsvLine(String csvLine) {
    String[] split = csvLine.split(";");
    return new RfIdScan(split[0], split[1], split[2]);
  }

  public String toCsvLine() {
    return rfid + ";" + workday + ";" + scanTime.format(TIME_FORMAT) + System.lineSeparator();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof RfIdScan rfIdScan)) return false;
    return Objects.equals(rfid, rfIdScan.rfid) &&
        Objects.equals(workday, rfIdScan.workday) &&
        Objects.equals(scanTime, rfIdScan.scanTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rfid, workday, scanTime);
  }


}
