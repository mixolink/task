package com.amituofo.task;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.api.Callback;
import com.amituofo.common.type.RunStatus;
import com.amituofo.task.ex.ArchiveException;

public interface TaskManagement {

//	void add(TaskDetail td) throws ArchiveException;
//
//	void add(TaskDetail td, TaskRuntimeStatusListener eventListener) throws ArchiveException;
//
//	void add(TaskDetail td, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
//			TaskRuntimeMessageListener runtimeMessageListener) throws ArchiveException;

	void execute(String id, TaskRuntimeStatusListener eventListener) throws ArchiveException;

	void execute(String id, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener) throws ArchiveException;

	void execute(String id, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log) throws ArchiveException;

	void execute(String id, TaskRuntimeStatusListener eventListener, Logger log) throws ArchiveException;

	void execute(TaskDetail taskDetail);

	void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener);

	void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener);

	void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener);

	void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log);

	void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, Logger log);

	TaskLauncher save(TaskDetail taskDetail) throws ArchiveException;

	TaskDetail get(String id) throws ArchiveException;

	boolean stop(String id);

	boolean kill(String id);

	void stopAll();

//	void removeAll();

	List<TaskThread> listActiveTasks();

	// List<TaskDetail> list(String groupId, String catalog);

//	void resume(String id);

	int getActiveCount();

	void setTaskPoolSize(int poolSize);

	int count() throws ArchiveException;

	void shutdown();

	void enableAutoClean(int cyclePeriod, int deadAfterTimeMillis);

	void disableAutoClean();

	List<TaskThread> clean(int deadAfterTimeMillis);

	List<TaskThread> cleanNow();

	boolean isAutoCleanEnabled();

	void removeCleanCallbacks();

	void addCleanCallback(Callback<List<TaskThread>> callback);

	void addGlobalTaskEventListener(GlobalTaskEventListener listener);

	void removeGlobalTaskEventListener(GlobalTaskEventListener listener);

	Collection<TaskDetail> list() throws ArchiveException;

	boolean remove(String id) throws ArchiveException;

	TaskThread getTaskThread(String id);

	boolean has(String id) throws ArchiveException;

	boolean isActive(String id);

	RunStatus getRunStatus(String id);

	// void registyProcessServicePoint(int port, ProcessServicePoint psp) throws
	// TaskException;
	void setDefaultLogger(Logger defaultLogger);

}
