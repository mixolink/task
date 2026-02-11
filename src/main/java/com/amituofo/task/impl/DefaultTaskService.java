package com.amituofo.task.impl;

import com.amituofo.common.api.Callback;
import com.amituofo.task.ProcessServicePoint;
import com.amituofo.task.TaskArchive;
import com.amituofo.task.TaskManagement;
import com.amituofo.task.TaskService;
import com.amituofo.task.ex.TaskException;

public class DefaultTaskService implements TaskService {
	private final TaskManagement taskManagement;

	public DefaultTaskService(int concurrent_task_count, TaskArchive taskArchive) {
		taskManagement = new TaskManagementImpl(concurrent_task_count, taskArchive);
	}

//	public DefaultTaskService(int concurrent_task_count, TaskArchive taskArchive, int port, ProcessServicePoint psp) throws TaskException {
//		taskManagement = new TaskManagementImpl(concurrent_task_count, taskArchive, port, psp);
//	}
	
	public DefaultTaskService(int concurrent_task_count, TaskArchive taskArchive, int enableProcessServicePointAtPort) throws Exception {
		taskManagement = new TaskManagementImpl(concurrent_task_count, taskArchive, enableProcessServicePointAtPort);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop(Callback<Void> callback) {
		taskManagement.shutdown();
	}

	@Override
	public TaskManagement getTaskManagement() {
		return taskManagement;
	}

}
