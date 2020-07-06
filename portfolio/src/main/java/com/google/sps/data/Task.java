package com.google.sps.data;

public final class Task {
	private final long id;
	private final String comment;
	private final long timestamp;
	private final String email;

	public Task(long id, String comment, long timestamp, String email) {
		this.id = id;
		this.comment = comment;
		this.timestamp = timestamp;
		this.email = email;
	}
}
