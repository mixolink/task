package com.amituofo.task;

import com.amituofo.common.type.RunStatus;

public interface TaskRuntimeStatusListener {
	TaskRuntimeStatusListener NOOP = new TaskRuntimeStatusListener() {
		@Override
		public void statusChanged(RunStatus status, TaskRuntimeStatus details) {
		}
	};

	public void statusChanged(RunStatus status, TaskRuntimeStatus details);
}
