package de.zaehlermann.timetracker;

import de.zaehlermann.timetracker.log.Logger;
import de.zaehlermann.timetracker.repository.RfidScanRepository;

import java.util.Scanner;

public class DataTracker {

  private static final RfidScanRepository RFID_SCAN_REPOSITORY = new RfidScanRepository();
  private static final Logger LOG = Logger.INSTANCE;

  public static void main(final String[] args) {

    LOG.info("==================================");
    LOG.info("Welcome to Work-Time-Tracker v.1.0");
    LOG.info("==================================");
    try {
      final Scanner scanner = new Scanner(System.in);
      while(true) {
        LOG.info("Waiting for input..");
        final String input = scanner.nextLine();
        if(input.equals("exit")) {
          LOG.info("Exit");
          scanner.close();
          break;
        }

        LOG.info(RFID_SCAN_REPOSITORY.saveScan(input));
      }
    }
    catch(final Exception e) {
      LOG.error("Exception before waiting for input: " + e);
    }
  }

}
