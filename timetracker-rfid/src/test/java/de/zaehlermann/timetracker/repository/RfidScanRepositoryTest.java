package de.zaehlermann.timetracker.repository;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RfidScanRepositoryTest {

  @Test
  void saveScan() {

    final MutableClock clock = new MutableClock(Instant.parse("2024-06-01T00:00:00Z"), ZoneId.systemDefault());
    final RfidScanRepository repo = new RfidScanRepository(clock);

    for (int i = 0; i < 100; i++) {
      repo.saveScan("1234567890");
      if (i % 2 == 0) {
        clock.plusDays(1);
      } else {
        clock.plusHours(7);
      }
    }

    assertEquals(100, repo.findAllRfIdScansByRfid("1234567890").size());

  }

}