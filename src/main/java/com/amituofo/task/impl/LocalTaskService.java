package com.amituofo.task.impl;

import java.io.IOException;

import com.amituofo.common.ex.ParseException;

public class LocalTaskService extends DefaultTaskService {

	public LocalTaskService(int concurrent_task_count, String configLocation, String ext) throws ParseException, IOException {
		super(concurrent_task_count, new LocalFileSystemTaskArchiveImpl(configLocation, ext, null));
	}

}
