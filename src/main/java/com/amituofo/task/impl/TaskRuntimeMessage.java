package com.amituofo.task.impl;

import java.io.Serializable;

import com.amituofo.task.TaskRuntimeMessageListener;

public class TaskRuntimeMessage implements TaskRuntimeMessageListener, Serializable {
	private String runtimeName;
	private String runtimeMessage;
	private String additionMessage = null;

	@Override
	public void updateRuntimeMessage(String runtimeMessage) {
		this.runtimeMessage = runtimeMessage;
	}

	@Override
	public void updateAdditionMessage(String additionMessage) {
		this.additionMessage = additionMessage;
	}

	@Override
	public void updateRuntimeName(String runtimeStatus) {
		this.runtimeName = runtimeStatus;
	}

	public String getRuntimeMessage() {
		return runtimeMessage;
	}

	public String getAdditionMessage() {
		return additionMessage;
	}

	public String getRuntimeName() {
		return runtimeName;
	}

}
