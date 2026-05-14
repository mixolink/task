package com.amituofo.task;

import org.slf4j.Logger;

import com.amituofo.common.kit.value.Value;
import com.amituofo.common.type.RunStatus;
import com.amituofo.common.util.ThreadUtils;
import com.amituofo.task.ex.TaskException;

/**
 * @author song
 */
public class DelegateTask extends TaskBase {
	private TaskBase[] tasks;

	public DelegateTask(TaskBase[] tasks) {
		super();
		this.tasks = tasks;
	}

	public void setDelegateTasks(TaskBase[] tasks) {
		this.tasks = tasks;
	}

	public TaskBase[] getDelegateTasks() {
		return tasks;
	}

	@Override
	public boolean initialize(TaskParameter parameter) throws TaskException {
		boolean ok = true;
		for (TaskBase task : tasks) {
			ok &= task.initialize(parameter);
		}
		return ok;
	}

	@Override
	public boolean prepare() throws TaskException {
		boolean ok = true;
		for (TaskBase task : tasks) {
			ok &= task.prepare();
		}
		return ok;
	}

	@Override
	public void execute() throws TaskException {
//		ThreadUtils.executeAwait(Task::execute, tasks);

		try {
			ThreadUtils.executeAwait(t -> {
				try {
					t.execute();
				} catch (TaskException e) {
					e.printStackTrace();
				}
			}, tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ExecResult finish() throws TaskException {
		Value<ExecResult> result = new Value<>(ExecResult.Success);
//		for (TaskBase task : tasks) {
//			result = task.finish();
//		}
		
		try {
			ThreadUtils.executeAwait(t -> {
				try {
					ExecResult result0 = t.finish();
					if (result0 != ExecResult.Success) {
						result.setValue(result0);
					}
				} catch (TaskException e) {
					e.printStackTrace();
				}
			}, tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.getValue();
	}

	@Override
	public void release() throws TaskException {
//		for (TaskBase task : tasks) {
//			task.release();
//		}

		try {
			ThreadUtils.executeAwait(t -> {
				try {
					t.release();
				} catch (TaskException e) {
					e.printStackTrace();
				}
			}, tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean tryStop() throws TaskException {
//		for (TaskBase task : tasks) {
//			ok &= task.tryStop();
//		}
		
		Value<Boolean> result = new Value<>(Boolean.TRUE);
		try {
			ThreadUtils.executeAwait(t -> {
				try {
					boolean result0 = t.tryStop();
					if (result0 != true) {
						result.setValue(result0);
					}
				} catch (TaskException e) {
					e.printStackTrace();
				}
			}, tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.getValue();
	}

	@Override
	public boolean kill() {
//		for (TaskBase task : tasks) {
//			ok &= task.kill();
//		}
		
		Value<Boolean> result = new Value<>(Boolean.TRUE);
		try {
			ThreadUtils.executeAwait(t -> {
				boolean result0 = t.kill();
				if (result0 != true) {
					result.setValue(result0);
				}
			}, tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.getValue();
	}

	@Override
	public Object getStatusDescription(RunStatus status) {
		return null;
	}

	@Override
	public void setLogger(Logger log) {
		super.setLogger(log);
		for (TaskBase task : tasks) {
			task.setLogger(log);
		}
	}

	@Override
	public void setTaskDetail(TaskDetail taskDetail) {
		super.setTaskDetail(taskDetail);
		for (TaskBase task : tasks) {
			task.setTaskDetail(taskDetail);
		}
	}

	@Override
	public void setRuntimeStatus(TaskRuntimeStatus taskRuntimeStatus) {
		super.setRuntimeStatus(taskRuntimeStatus);
		for (TaskBase task : tasks) {
			task.setRuntimeStatus(taskRuntimeStatus);
		}
	}

	@Override
	public void resetStatus(RunStatus status, Object meta) {
		super.resetStatus(status, meta);
		for (TaskBase task : tasks) {
			task.resetStatus(status, meta);
		}
	}

	@Override
	public void resetStatus(RunStatus status) {
		super.resetStatus(status);
		for (TaskBase task : tasks) {
			task.resetStatus(status);
		}
	}

//	@Override
//	public void setRuntimeProgressListener(TaskRuntimeProgressListener progress) {
//		super.setRuntimeProgressListener(progress);
//		for (TaskBase task : tasks) {
//			task.setRuntimeProgressListener(progress);
//		}
//	}
//
//	@Override
//	public void setRuntimeMessageListener(TaskRuntimeMessageListener runtimeMessageListener) {
//		super.setRuntimeMessageListener(runtimeMessageListener);
//		for (TaskBase task : tasks) {
//			task.setRuntimeMessageListener(runtimeMessageListener);
//		}
//	}
//
//	@Override
//	public void setRuntimePerformanceListener(TaskRuntimePerformanceListener performanceListener) {
//		super.setRuntimePerformanceListener(performanceListener);
//		for (TaskBase task : tasks) {
//			task.setRuntimePerformanceListener(performanceListener);
//		}
//	}
}
