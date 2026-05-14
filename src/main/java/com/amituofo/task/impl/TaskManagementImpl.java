package com.amituofo.task.impl;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.amituofo.common.api.Callback;
import com.amituofo.common.kit.remote.RMIRegistry;
import com.amituofo.common.type.RunStatus;
import com.amituofo.task.GlobalTaskEventListener;
import com.amituofo.task.ProcessServicePoint;
import com.amituofo.task.TaskArchive;
import com.amituofo.task.TaskDetail;
import com.amituofo.task.TaskLauncher;
import com.amituofo.task.TaskManagement;
import com.amituofo.task.TaskRuntimeMessageListener;
import com.amituofo.task.TaskRuntimePerformanceListener;
import com.amituofo.task.TaskRuntimeProgressListener;
import com.amituofo.task.TaskRuntimeStatusListener;
import com.amituofo.task.TaskThread;
import com.amituofo.task.TaskThreadManagement;
import com.amituofo.task.ex.ArchiveException;
import com.amituofo.task.ex.TaskException;

public class TaskManagementImpl implements TaskManagement {
	private final TaskArchive archive;
	// private final List<TaskDetails> tasks = new ArrayList<TaskDetails>();
	// private final Map<String, TaskExecuter> tasks = new HashMap<String,
	// TaskExecuter>();
	private final TaskThreadManagement tasksThreadMaster;
	private RMIRegistry rmi = null;

	private Logger defaultLogger = null;

	public TaskManagementImpl(int taskPoolSize, TaskArchive archive) {
		this.tasksThreadMaster = new TaskThreadManagementImpl(taskPoolSize);
		this.archive = archive;
	}

	public TaskManagementImpl(int taskPoolSize, TaskArchive archive, int enableProcessServicePointAtPort) throws Exception {
		this.tasksThreadMaster = new TaskThreadManagementImpl(taskPoolSize);
		this.archive = archive;

		this.registyProcessServicePoint(enableProcessServicePointAtPort, new DefaultProcessServicePoint(tasksThreadMaster));
	}

	private void registyProcessServicePoint(int port, ProcessServicePoint psp) throws TaskException {
		rmi = new RMIRegistry(port);

		try {
			rmi.bind(ProcessServicePoint.class.getSimpleName(), psp);
		} catch (Exception e) {
//			e.printStackTrace();
			throw new TaskException(e);
		}
	}

//	@Override
//	public void add(TaskDetail td) throws ArchiveException {
//		add(td, null);
//	}
//
//	@Override
//	public void add(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener) throws ArchiveException {
//		if (taskDetail == null) {
//			return;
//		}
//
//		archive.save(taskDetail);
//		tasksThreadMaster.execute(taskDetail, eventListener, new TaskRuntimeProgress(), new TaskRuntimePerformance(), new TaskRuntimeMessage(), null);
//	}
//
//	@Override
//	public void add(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener,
//			TaskRuntimePerformanceListener performanceListener, TaskRuntimeMessageListener runtimeMessageListener) throws ArchiveException {
//
//		if (taskDetail == null) {
//			return;
//		}
//
//		archive.save(taskDetail);
//		execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener, null);
//	}

	@Override
	public void setDefaultLogger(Logger defaultLogger) {
		this.defaultLogger = defaultLogger;
	}

	@Override
	public void execute(TaskDetail td) {
		execute(td, null);
	}

	@Override
	public void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener) {
		execute(taskDetail, eventListener, new TaskRuntimeProgress(), new TaskRuntimePerformance(), new TaskRuntimeMessage(), null);
	}

	@Override
	public void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, Logger log) {
		execute(taskDetail, eventListener, new TaskRuntimeProgress(), new TaskRuntimePerformance(), new TaskRuntimeMessage(), log);
	}

	@Override
	public void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener) {
		execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener, null);
	}

	@Override
	public void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener) {
		execute(taskDetail, eventListener, progressListener, null, null, null);
	}

	@Override
	public void execute(String id, TaskRuntimeStatusListener eventListener) throws ArchiveException {
		TaskDetail taskDetail = archive.get(id);
		if (taskDetail != null) {
			execute(taskDetail, eventListener, new TaskRuntimeProgress(), new TaskRuntimePerformance(), new TaskRuntimeMessage(), null);
		}
	}

	@Override
	public void execute(String id, TaskRuntimeStatusListener eventListener, Logger log) throws ArchiveException {
		TaskDetail taskDetail = archive.get(id);
		execute(taskDetail, eventListener, new TaskRuntimeProgress(), new TaskRuntimePerformance(), new TaskRuntimeMessage(), log);
	}

	@Override
	public void execute(String id, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener) throws ArchiveException {
		TaskDetail taskDetail = archive.get(id);
		execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener);
	}

	@Override
	public void execute(String id, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log) throws ArchiveException {
		TaskDetail taskDetail = archive.get(id);
		execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener, log);
	}

	@Override
	public void execute(TaskDetail taskDetail, TaskRuntimeStatusListener eventListener, TaskRuntimeProgressListener progressListener, TaskRuntimePerformanceListener performanceListener,
			TaskRuntimeMessageListener runtimeMessageListener, Logger log) {
		if (taskDetail != null) {
			if (log == null) {
				log = defaultLogger;
			}
			tasksThreadMaster.execute(taskDetail, eventListener, progressListener, performanceListener, runtimeMessageListener, log);
		}
	}

	@Override
	public TaskLauncher save(TaskDetail taskDetail) throws ArchiveException {
		archive.save(taskDetail);

		return new DefaultTaskLauncher(this, taskDetail);
	}

	@Override
	public void disableAutoClean() {
		tasksThreadMaster.disableAutoClean();
	}

	@Override
	public void enableAutoClean(int cyclePeriod, final int deadAfterTimeMillis) {
		tasksThreadMaster.enableAutoClean(cyclePeriod, deadAfterTimeMillis);
	}

	@Override
	public void addCleanCallback(final Callback<List<TaskThread>> callback) {
		tasksThreadMaster.addCleanCallback(callback);
	}

	@Override
	public void removeCleanCallbacks() {
		tasksThreadMaster.removeCleanCallbacks();
	}

	@Override
	public boolean isAutoCleanEnabled() {
		return tasksThreadMaster.isAutoCleanEnabled();
	}

	@Override
	public synchronized List<TaskThread> clean(int deadAfterTimeMillis) {
		return tasksThreadMaster.clean(deadAfterTimeMillis);
	}

	@Override
	public List<TaskThread> cleanNow() {
		return tasksThreadMaster.clean(0);
	}

	@Override
	public void setTaskPoolSize(int poolSize) {
		tasksThreadMaster.setTaskPoolSize(poolSize);
	}

//	@Override
//	public void resume(String id) {
//		tasksMgr.resume(id);
//	}

	@Override
	public TaskDetail get(String id) throws ArchiveException {
		return archive.get(id);
	}

	@Override
	public boolean remove(String id) throws ArchiveException {
		TaskThread th = tasksThreadMaster.get(id);
		if (th != null) {
			if (th.getRuntimeStatus().getStatus().isRunningStatus()) {
				return false;
			}
		}
		return archive.delete(id);
	}

	@Override
	public boolean stop(String id) {
		return tasksThreadMaster.interrupt(id);
	}

	@Override
	public boolean kill(String id) {
		return tasksThreadMaster.kill(id);
	}

	@Override
	public synchronized void stopAll() {
		tasksThreadMaster.interruptAll();

		// while(tasksMgr.getActiveCount()>0) {
		// try {
		// Thread.sleep(50);
		// } catch (InterruptedException e) {
		// }
		// }
	}

//	@Override
//	public synchronized void removeAll() {
//		stopAll();
//
//		archive.deleteAll();
//	}

	@Override
	public Collection<TaskDetail> list() throws ArchiveException {
		Collection<TaskDetail> list = archive.list();
//		for (TaskDetail taskDetail : list) {
//			TaskThread tt = tasksMgr.get(taskDetail.getId());
//			if(tt!=null) {
//				taskDetail.
//			}
//		}

		return list;
	}

	@Override
	public int count() throws ArchiveException {
		return archive.count();
	}

	// @Override
	// public List<TaskDetail> list(String groupId, String catalog) {
	// return archive.list(groupId, catalog);
	// }

	@Override
	public List<TaskThread> listActiveTasks() {
		return tasksThreadMaster.list();
	}

	@Override
	public TaskThread getTaskThread(String id) {
		return tasksThreadMaster.get(id);
	}

	@Override
	public RunStatus getRunStatus(String id) {
		TaskThread t = tasksThreadMaster.get(id);
		if (t == null) {
			return RunStatus.Stoped;
		}
		return t.getRuntimeStatus().getStatus();
	}

	@Override
	public boolean isActive(String id) {
		TaskThread t = tasksThreadMaster.get(id);
		if (t == null) {
			return false;
		}

		return t.isAlive();
	}

	@Override
	public int getActiveCount() {
		return tasksThreadMaster.getActiveCount();
	}

	@Override
	public void shutdown() {
		tasksThreadMaster.shutdown();
	}

	@Override
	public void addGlobalTaskEventListener(GlobalTaskEventListener listener) {
		tasksThreadMaster.addGlobalTaskEventListener(listener);
	}

	@Override
	public void removeGlobalTaskEventListener(GlobalTaskEventListener listener) {
		tasksThreadMaster.addGlobalTaskEventListener(listener);
	}

	@Override
	public boolean has(String id) throws ArchiveException {
		return archive.has(id);
	}

}
