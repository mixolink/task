package com.amituofo.task;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.type.RunStatus;

public abstract class TaskBase implements Task {
	protected TaskRuntimeProgressListener progressListener;
	protected TaskRuntimeMessageListener runtimeMessageListener;
	protected TaskRuntimePerformanceListener performanceListener;

	protected TaskDetail taskDetail;
	private TaskRuntimeStatus taskRuntimeStatus;
	
	protected Logger log;

	public void setLogger(Logger log) {
		this.log = log;
	}

	public void setTaskDetail(TaskDetail taskDetail) {
		this.taskDetail = taskDetail;
	}

	public void setRuntimeStatus(TaskRuntimeStatus taskRuntimeStatus) {
		this.taskRuntimeStatus = taskRuntimeStatus;
	}

	public void resetStatus(RunStatus status, Object meta) {
		taskRuntimeStatus.setStatus(status, meta);
	}

	public void resetStatus(RunStatus status) {
		taskRuntimeStatus.setStatus(status);
	}

	public RunStatus getStatus() {
		return taskRuntimeStatus.getStatus();
	}

	public void setRuntimeProgressListener(TaskRuntimeProgressListener progress) {
		this.progressListener = progress;
	}

	public void setRuntimeMessageListener(TaskRuntimeMessageListener runtimeMessageListener) {
		this.runtimeMessageListener = runtimeMessageListener;
	}

	public void setRuntimePerformanceListener(TaskRuntimePerformanceListener performanceListener) {
		this.performanceListener = performanceListener;
	}

	public TaskDetail getTaskDetail() {
		return taskDetail;
	}

	public String getTaskId() {
		return taskDetail.getId();
	}

	public abstract Object getStatusDescription(RunStatus status);

}
