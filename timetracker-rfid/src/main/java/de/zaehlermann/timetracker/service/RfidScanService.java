package de.zaehlermann.timetracker.service;

import de.zaehlermann.timetracker.repository.RfidScanRepository;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;

public class RfidScanService {

  private static final RfidScanRepository RFID_SCAN_REPOSITORY = new RfidScanRepository();

  @NonNull
  public List<String> findAllRfids() {
    return RFID_SCAN_REPOSITORY.findAllRfids();
  }

}
