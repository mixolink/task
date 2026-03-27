package com.amituofo.task;

public class TaskKey {
	String taskID;
	String threadID;

	public TaskKey(String taskID, String threadID) {
		super();
		this.taskID = taskID;
		this.threadID = threadID;
	}

	public String getTaskID() {
		return taskID;
	}

	public String getThreadID() {
		return threadID;
	}
}
