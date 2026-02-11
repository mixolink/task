package com.amituofo.task.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import com.amituofo.common.type.RunStatus;
import com.amituofo.common.util.StringUtils;
import com.amituofo.task.ProcessServicePoint;
import com.amituofo.task.TaskRuntimeMessageListener;
import com.amituofo.task.TaskRuntimePerformanceListener;
import com.amituofo.task.TaskRuntimeProgressListener;
import com.amituofo.task.TaskRuntimeStatus;
import com.amituofo.task.TaskRuntimeStatusListener;
import com.amituofo.task.TaskThread;
import com.amituofo.task.TaskThreadManagement;

public class DefaultProcessServicePoint extends UnicastRemoteObject implements ProcessServicePoint {

	private TaskThreadManagement tasksMgr;

	public DefaultProcessServicePoint(TaskThreadManagement tasksMgr) throws RemoteException {
		super();
		this.tasksMgr = tasksMgr;
	}

	@Override
	public void updatePerformance(String id, TaskRuntimePerformance performance) {
		if (StringUtils.isEmpty(id) || performance == null) {
			return;
		}

		TaskThread t = tasksMgr.get(id);
		if (t != null) {
			TaskRuntimePerformanceListener l = t.getPerformanceListener();
			l.updatePerformance(performance.getPerformance());
			l.updateRemainSecond(performance.getRemainSecond());
			l.updateWorkerCount(performance.getWorkerCount());
		}
	}

	@Override
	public void updateMessage(String id, TaskRuntimeMessage message) {
		if (StringUtils.isEmpty(id) || message == null) {
			return;
		}

		TaskThread t = tasksMgr.get(id);
		if (t != null) {
			TaskRuntimeMessageListener l = t.getRuntimeMessageListener();
			l.updateRuntimeMessage(message.getRuntimeMessage());
			l.updateAdditionMessage(message.getAdditionMessage());
			l.updateRuntimeName(message.getRuntimeName());
		}
	}

	@Override
	public void updateStatus(String id, RunStatus status, TaskRuntimeStatus details) {
		if (StringUtils.isEmpty(id)) {
			return;
		}

		TaskThread t = tasksMgr.get(id);
		if (t != null) {
			TaskRuntimeStatus rs = t.getRuntimeStatus();

			if (details != null) {
				rs.setCause(details.getCause());
				rs.setExecResult(details.getExecResult());
				rs.setStatus(details.getStatus(), details.getMeta());
			} else {
				rs.setStatus(status);
			}
		}
	}

	@Override
	public void updateProgress(String id, TaskRuntimeProgress progress) {
		if (StringUtils.isEmpty(id)) {
			return;
		}

		TaskThread t = tasksMgr.get(id);
		if (t == null) {

		} else {
			TaskRuntimeProgressListener l = t.getProgressListener();
			l.setMaxProgress(progress.getMaxProgress());
			l.updateProgress(progress.getProgress());
			l.updateProgressMode(progress.getProgressMode());
		}
	}

}
