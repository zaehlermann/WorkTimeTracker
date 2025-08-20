package de.elementcamper;

import de.elementcamper.repository.RfidScanRepository;

import java.io.IOException;
import java.util.Scanner;

public class DataTracker {

  private static final String baseDir = "C:/WorkTimeTracker";
  private static final RfidScanRepository RFID_SCAN_REPOSITORY = new RfidScanRepository(baseDir);

  public static void main(String[] args) throws IOException {

    System.out.println("============================");
    System.out.println("Welcome to Work-Time-Tracker");
    System.out.println("============================");

    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Waiting for input..");
      String input = scanner.nextLine();
      if (input.equals("exit")) {
        System.out.println("Exit");
        scanner.close();
        break;
      }

      RFID_SCAN_REPOSITORY.saveScan(input);
    }
  }

}
