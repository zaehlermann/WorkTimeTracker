package de.zaehlermann.timetracker.repository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

class MutableClock extends Clock {
  private Instant instant;
  private final ZoneId zone;

  public MutableClock(final Instant initial, final ZoneId zone) {
    this.instant = initial;
    this.zone = zone;
  }

  public void setInstant(final Instant newInstant) {
    this.instant = newInstant;
  }

  public void plusDays(final long days) {
    this.instant = this.instant.plusSeconds(days * 60 * 60 * 24);
  }

  public void plusHours(final long hours) {
    this.instant = this.instant.plusSeconds(hours * 60 * 60);
  }

  @Override
  public ZoneId getZone() {
    return zone;
  }

  @Override
  public Clock withZone(final ZoneId zone) {
    return new MutableClock(instant, zone);
  }

  @Override
  public Instant instant() {
    return instant;
  }
}