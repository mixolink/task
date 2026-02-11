package com.amituofo.task.impl;

import java.io.Serializable;

import com.amituofo.task.TaskRuntimePerformanceListener;

public class TaskRuntimePerformance implements TaskRuntimePerformanceListener, Serializable {
	private int workerCount = 1;
	private Object performance = "-";
	private int remainSecond;

	@Override
	public void updatePerformance(Object performance) {
		this.performance = performance;
	}

	@Override
	public void updateWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public Object getPerformance() {
		return performance;
	}

	public int getRemainSecond() {
		return remainSecond;
	}

	@Override
	public void updateRemainSecond(int remainSecond) {
		this.remainSecond = remainSecond;
	}

}
