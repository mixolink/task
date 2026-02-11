package com.amituofo.task.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;

import com.amituofo.common.api.Callback;
import com.amituofo.common.type.RunStatus;
import com.amituofo.task.GlobalTaskEventListener;
import com.amituofo.task.TaskDetail;
import com.amituofo.task.TaskRuntimeMessageListener;
import com.amituofo.task.TaskRuntimePerformanceListener;
import com.amituofo.task.TaskRuntimeProgressListener;
import com.amituofo.task.TaskRuntimeStatus;
import com.amituofo.task.TaskRuntimeStatusListener;
import com.amituofo.task.TaskThread;
import com.amituofo.task.TaskThreadManagement;

public class TaskThreadManagementImpl implements TaskThreadManagement {
	private static int maximumPoolSize = 5;
	private static int corePoolSize = 5;
	private final Map<String, TaskThread> threads = new ConcurrentHashMap<String, TaskThread>();

	private BlockingQueue<Runnable> threadQueue = new LinkedBlockingDeque<Runnable>();

	private Timer cleanTimer = null;

	private final List<Callback<List<TaskThread>>> callbacks = new ArrayList<>();

	private final List<GlobalTaskEventListener> globalTaskEventListeners = new ArrayList<>();

	// final ThreadFactory TF = new ThreadFactory() {
	//
	// @Override
	// public Thread newThread(Runnable r) {
	// TaskExecuter te = (TaskExecuter)((ThreadPoolExecutor.Worker) r).getFirstQueuedThread();
	//// TaskDetail td = ((ThreadPoolExecutor.Worker) r).getFirstQueuedThread().getTaskDetails();
	// // TaskThread t = new TaskThread(null, r);
	// //
	// // threads.put(td.getId(), t);
	//
	//
	// return new Thread(r);
	// }
	// };

	private ThreadPoolExecutor threadExecutor;

	public TaskThreadManagementImpl(int poolSize) {
		corePoolSize = maximumPoolSize = (poolSize <= 0 ? 1 : poolSize);

		createThreadPool();

		enableAutoClean(3000, 1000);
	}

	private void createThreadPool() {
		threadQueue = new LinkedBlockingDeque<Runnable>();
		threadExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 999, TimeUnit.DAYS, threadQueue);
	}

	public void setTaskPoolSize(int poolSize) {
		corePoolSize = maximumPoolSize = (poolSize <= 0 ? 1 : poolSize);
		threadExecutor.setCorePoolSize(corePoolSize);
		threadExecutor.setMaximumPoolSize(maximumPoolSize);
	}

	@Override
	public void execute(final TaskDetail td, final TaskRuntimeStatusListener eventListener, final TaskRuntimeProgressListener progressListener,
			final TaskRuntimePerformanceListener performanceListener, final TaskRuntimeMessageListener runtimeMessageListener, Logger log) {
		synchronized (threads) {
			TaskThread t = get(td.getId());

			if (t != null) {
				RunStatus status = t.getRuntimeStatus().getStatus();
				if (RunStatus.isStopStatus(status)) {
					// 自动清除
//					threads.remove(td.getId());
				} else {
					return;
				}
			}

			TaskThread te = new TaskThread(td);
			if (log != null) {
				te.setLogger(log);
			}

			if (globalTaskEventListeners.size() == 0 && eventListener != null) {
				te.setEventListener(eventListener);
			} else if (globalTaskEventListeners.size() > 0 || eventListener != null) {
				te.setEventListener(new TaskRuntimeStatusListener() {
					@Override
					public void statusChanged(RunStatus status, TaskRuntimeStatus details) {
						if (eventListener != null) {
							eventListener.statusChanged(status, details);
						}

						if (globalTaskEventListeners.size() > 0) {
							for (GlobalTaskEventListener globalTaskEventListener : globalTaskEventListeners) {
								globalTaskEventListener.statusChanged(td.getId(), status);
							}
						}
					}
				});
			}

			te.setProgressListener(progressListener);
			te.setPerformanceListener(performanceListener);
			te.setRuntimeMessageListener(runtimeMessageListener);
			threads.put(td.getId(), te);
//			TaskRuntimeStatus runtimeStatus = te.getRuntimeStatus();
//			runtimeStatus.setStatus(TaskStatus.Pending, null);

			threadExecutor.execute(te);
		}
	}

	@Override
	public TaskThread get(String id) {
		return threads.get(id);
	}

	@Override
	public boolean interrupt(String id) {
		TaskThread t = threads.get(id);
		if (t != null) {
			t.interrupt();
//			 threads.remove(id);
		}
		return true;
	}

	@Override
	public boolean kill(String id) {
		TaskThread t = threads.get(id);
		if (t != null) {
			return t.kill();
//			 threads.remove(id);
		}

		return true;
	}

	@Override
	public void interruptAll() {
		synchronized (threads) {
			threadQueue.clear();
			Collection<TaskThread> ts = threads.values();
			for (TaskThread t : ts) {
				t.interrupt();
			}
//			 threads.clear();
		}

		createThreadPool();
	}

	@Override
	public int getActiveCount() {
		return threadExecutor.getActiveCount();
	}

	@Override
	public List<TaskThread> list() {
		List<TaskThread> list = new ArrayList<TaskThread>(threads.values());
//		Collection<TaskThread> ts = threads.values();
//		for (TaskThread t : ts) {
//			list.add(t);
//		}
		
		return list;
	}

	@Override
	public void resume(String id) {
		TaskThread t = threads.get(id);
		if (t != null && !t.isAlive()) {
			t.start();
		}
	}

	@Override
	public void shutdown() {
		disableAutoClean();
		interruptAll();
		threadExecutor.shutdownNow();
	}

	@Override
	public void disableAutoClean() {
		if (cleanTimer != null) {
			cleanTimer.cancel();
			cleanTimer = null;
		}
	}

	@Override
	public void enableAutoClean(int cyclePeriod, final int deadAfterTimeMillis) {
		if (cyclePeriod > 0) {
			disableAutoClean();

			cleanTimer = new Timer("Task-Auto-Clean");
			cleanTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					clean(deadAfterTimeMillis);
				}
			}, 500, cyclePeriod);
		}
	}

	@Override
	public boolean isAutoCleanEnabled() {
		return cleanTimer != null;
	}

	@Override
	public void addCleanCallback(final Callback<List<TaskThread>> callback) {
		callbacks.add(callback);
	}

	@Override
	public void removeCleanCallbacks() {
		callbacks.clear();
	}

	@Override
	public synchronized List<TaskThread> clean(int deadAfterTimeMillis) {
		List<TaskThread> tobeRemove = new ArrayList<TaskThread>();
		if (threads.isEmpty()) {
			return tobeRemove;
		}
		// TODO
		Collection<TaskThread> tasks = threads.values();
		for (TaskThread task : tasks) {
			RunStatus status = task.getRuntimeStatus().getStatus();
			if (RunStatus.isStopStatus(status)) {
				if ((System.currentTimeMillis() - task.getRuntimeStatus().getEndTimeMillis()) > deadAfterTimeMillis) {
					tobeRemove.add(task);
				}
			}
		}

		if (tobeRemove.size() > 0) {
			for (TaskThread task : tobeRemove) {
				threads.remove(task.getTaskDetails().getId());
			}

			if (tobeRemove.size() > 0 && callbacks.size() > 0) {
				for (Callback<List<TaskThread>> callback : callbacks) {
					callback.callback(tobeRemove);
				}
			}
		}

		return tobeRemove;
	}

	@Override
	public void addGlobalTaskEventListener(GlobalTaskEventListener listener) {
		globalTaskEventListeners.add(listener);
	}

	@Override
	public void removeGlobalTaskEventListener(GlobalTaskEventListener listener) {
		globalTaskEventListeners.remove(listener);
	}

}
