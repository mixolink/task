package com.amituofo.task;

public interface TaskRuntimeProgressListener {
	public final static TaskRuntimeProgressListener NOOP = new TaskRuntimeProgressListener() {
		@Override
		public void setMaxProgress(long max) {
		}

		@Override
		public void updateProgress(long step) {
		}

		@Override
		public void updateProgressTo100Percent() {
		}

		@Override
		public void updateProgressMode(ProgressMode progressMode) {
		}
	};

	void setMaxProgress(long max);

	void updateProgress(long step);

	void updateProgressTo100Percent();

	void updateProgressMode(ProgressMode progressMode);
}
