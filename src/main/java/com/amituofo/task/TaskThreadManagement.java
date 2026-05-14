package com.amituofo.task;

import java.util.List;

import org.slf4j.Logger;

import com.amituofo.common.api.Callback;

public interface TaskThreadManagement {

	void execute(TaskDetail td, TaskRuntimeStatusListener eventListener,
			TaskRuntimeProgressListener progressListener,
			TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log);

	TaskThread get(String id);

	boolean interrupt(String id);

	boolean kill(String id);

	void interruptAll();

	List<TaskThread> list();

	int getActiveCount();

	void resume(String id);
	
	void setTaskPoolSize(int poolSize);

	void shutdown();

	List<TaskThread> clean(int deadAfterTimeMillis);

	void enableAutoClean(int cyclePeriod, int deadAfterTimeMillis);

	void disableAutoClean();

	boolean isAutoCleanEnabled();

	void removeCleanCallbacks();

	void addCleanCallback(Callback<List<TaskThread>> callback);

	void addGlobalTaskEventListener(GlobalTaskEventListener listener);

	void removeGlobalTaskEventListener(GlobalTaskEventListener listener);

}
