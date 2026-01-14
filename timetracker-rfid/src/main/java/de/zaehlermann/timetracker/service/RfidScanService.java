package de.zaehlermann.timetracker.service;

import java.util.List;

import jakarta.annotation.Nonnull;

import de.zaehlermann.timetracker.repository.RfidScanRepository;

public class RfidScanService {

  private static final RfidScanRepository RFID_SCAN_REPOSITORY = new RfidScanRepository();

  @Nonnull
  public List<String> findAllRfids() {
    return RFID_SCAN_REPOSITORY.findAllRfids();
  }

}
