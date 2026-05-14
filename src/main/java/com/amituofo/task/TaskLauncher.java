package com.amituofo.task;

import org.slf4j.Logger;

import com.amituofo.task.TaskManagement;
import com.amituofo.task.TaskRuntimeMessageListener;
import com.amituofo.task.TaskRuntimePerformanceListener;
import com.amituofo.task.TaskRuntimeProgressListener;
import com.amituofo.task.TaskRuntimeStatusListener;
import com.amituofo.task.ex.ArchiveException;

public interface TaskLauncher {
	void execute(TaskRuntimeStatusListener eventListener) throws ArchiveException;

	void execute(TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener) throws ArchiveException;

	void execute(TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log) throws ArchiveException;

	void execute(TaskRuntimeStatusListener eventListener, Logger log) throws ArchiveException;

	default void execute() throws ArchiveException {
		execute(null);
	}

}
