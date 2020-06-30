package com.google.sps.data;

public final class Task {
	private final long id;
	private final String comment;
	private final long timestamp;

	public Task(long id, String comment, long timestamp) {
		this.id = id;
		this.comment = comment;
		this.timestamp = timestamp;
	}
}
