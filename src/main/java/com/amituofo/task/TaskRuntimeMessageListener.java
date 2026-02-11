package com.amituofo.task;

public interface TaskRuntimeMessageListener {
	public final static TaskRuntimeMessageListener NOOP = new TaskRuntimeMessageListener() {
		@Override
		public void updateRuntimeMessage(String msg) {
		}

		@Override
		public void updateAdditionMessage(String msg) {
		}

		@Override
		public void updateRuntimeName(String name) {
		}
	};

	void updateRuntimeMessage(String msg);

	void updateAdditionMessage(String msg);

	void updateRuntimeName(String name);
}
