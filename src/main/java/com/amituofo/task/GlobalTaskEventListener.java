package com.amituofo.task;

import com.amituofo.common.type.RunStatus;

public interface GlobalTaskEventListener {
	public void statusChanged(String id, RunStatus status);
}
