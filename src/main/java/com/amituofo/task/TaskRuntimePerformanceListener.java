package com.amituofo.task;

public interface TaskRuntimePerformanceListener {
	public final static TaskRuntimePerformanceListener NOOP = new TaskRuntimePerformanceListener() {
		@Override
		public void updatePerformance(Object performance) {
		}

		@Override
		public void updateWorkerCount(int workerSize) {
		}

		@Override
		public void updateRemainSecond(int remainSecond) {
		}
	};

	void updatePerformance(Object performance);

	void updateWorkerCount(int workerSize);

	void updateRemainSecond(int remainSecond);
}
