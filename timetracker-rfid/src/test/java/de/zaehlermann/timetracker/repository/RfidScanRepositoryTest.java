package de.zaehlermann.timetracker.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class RfidScanRepositoryTest {

  @Test
  @Disabled("Only for manual testing, as it writes files")
  void saveScan() {

    final MutableClock clock = new MutableClock(Instant.parse("2024-06-01T00:00:00Z"), ZoneId.systemDefault());
    final RfidScanRepository repo = new RfidScanRepository(clock);

    for(int i = 0; i < 100; i++) {
      repo.saveScan("1234567890");
      if(i % 2 == 0) {
        clock.plusDays(1);
      }
      else {
        clock.plusHours(7);
      }
    }

    assertEquals(100, repo.findAllRfIdScansByRfid("1234567890", null, null).size());

  }

}