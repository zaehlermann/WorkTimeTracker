package de.zaehlermann;

import de.zaehlermann.repository.RfidScanRepository;

import java.io.IOException;
import java.util.Scanner;

public class DataTracker {

  private static final String baseDir = "C:/WorkTimeTracker";
  private static final RfidScanRepository RFID_SCAN_REPOSITORY = new RfidScanRepository(baseDir);

  public static void main(final String[] args) throws IOException {

    System.out.println("============================");
    System.out.println("Welcome to Work-Time-Tracker");
    System.out.println("============================");

    final Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Waiting for input..");
      final String input = scanner.nextLine();
      if (input.equals("exit")) {
        System.out.println("Exit");
        scanner.close();
        break;
      }

      RFID_SCAN_REPOSITORY.saveScan(input);
    }
  }

}
