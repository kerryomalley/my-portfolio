package com.google.sps.data;

public final class Task {
  private final long id;
  private final String comment;
  private final long timestamp;
  private final String userEmail;

  public Task(long id, String comment, long timestamp, String userEmail) {
    this.id = id;
    this.comment = comment;
    this.timestamp = timestamp;
    this.userEmail = userEmail;
  }
}
