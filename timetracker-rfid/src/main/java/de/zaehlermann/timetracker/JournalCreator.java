package de.zaehlermann.timetracker;

import java.io.IOException;
import java.util.Scanner;

import de.zaehlermann.timetracker.log.Logger;
import de.zaehlermann.timetracker.service.JournalService;

public class JournalCreator {

  private static final Logger LOG = Logger.INSTANCE;
  private static final JournalService JOURNAL_SERVICE = new JournalService();

  public static void main(final String[] args) throws IOException {
    LOG.info("====================================");
    LOG.info("Welcome to Work-Time-Journal-Creator");
    LOG.info("====================================");

    final Scanner scanner = new Scanner(System.in);
    while(true) {
      LOG.info("Waiting for input..");
      final String input = scanner.nextLine();
      if(input.equals("exit")) {
        LOG.info("Exit");
        scanner.close();
        break;
      }

      final String journal = JOURNAL_SERVICE.createJournal(input);
      LOG.info(journal);
    }
  }
}
