package com.amituofo.task.impl;

import org.apache.logging.log4j.Logger;

import com.amituofo.task.TaskDetail;
import com.amituofo.task.TaskLauncher;
import com.amituofo.task.TaskManagement;
import com.amituofo.task.TaskRuntimeMessageListener;
import com.amituofo.task.TaskRuntimePerformanceListener;
import com.amituofo.task.TaskRuntimeProgressListener;
import com.amituofo.task.TaskRuntimeStatusListener;
import com.amituofo.task.ex.ArchiveException;

public class DefaultTaskLauncher implements TaskLauncher {

	private TaskManagement tm;
	private TaskDetail taskDetail;

	protected DefaultTaskLauncher(TaskManagement tm, TaskDetail taskDetail) {
		this.tm = tm;
		this.taskDetail = taskDetail;
	}

	@Override
	public void execute(TaskRuntimeStatusListener eventListener) throws ArchiveException {
		tm.execute(taskDetail, eventListener);
	}

	@Override
	public void execute(TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener) throws ArchiveException {
		tm.execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener);
	}

	@Override
	public void execute(TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log) throws ArchiveException {
		tm.execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener, log);
	}

	@Override
	public void execute(TaskRuntimeStatusListener eventListener, Logger log) throws ArchiveException {
		tm.execute(taskDetail, eventListener, log);
	}

}
