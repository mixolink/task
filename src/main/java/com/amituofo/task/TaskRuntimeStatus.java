package com.amituofo.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amituofo.common.type.RunStatus;
import com.amituofo.common.type.RuntimeNames;

public class TaskRuntimeStatus implements Serializable {
	private RunStatus status = RunStatus.Pending;
//	private RuntimeStatus runtimeStatus = RuntimeStatus.Normal;
	private long statusUpdateTimeMillis = System.currentTimeMillis();
	private long startTimeMillis = System.currentTimeMillis();
	private long endTimeMillis = 0L;
	private ExecResult execResult = null;
	private Object meta;
	private Throwable cause;
//	private long lastUpdatetime = System.currentTimeMillis();

	private List<TaskRuntimeStatusListener> runtimeStatusListeners = null;

	public RunStatus getStatus() {
		return status;
	}

//	public RuntimeStatus getRuntimeStatus() {
//		return runtimeStatus;
//	}

//	public void setRuntimeStatus(RuntimeStatus runtimeStatus) {
//		if (this.runtimeStatus != runtimeStatus) {
//			this.runtimeStatus = runtimeStatus;
//			this.statusUpdateTimeMillis = System.currentTimeMillis();
//
////			if (runtimeStatusListeners != null) {
////				for (TaskRuntimeStatusListener listener : runtimeStatusListeners) {
////					listener.statusChanged(status, this);
////				}
////			}
//		}
//	}

	public void setStatus(RunStatus status) {
		this.setStatus(status, null);
	}

	public void setStatus(RunStatus status, Object meta) {
		this.meta = meta;

		if (this.status != status) {
			this.status = status;
			this.statusUpdateTimeMillis = System.currentTimeMillis();

			if (runtimeStatusListeners != null) {
				for (TaskRuntimeStatusListener listener : runtimeStatusListeners) {
					listener.statusChanged(status, this);
				}
			}
		}
	}

	public Object getMeta() {
		return meta;
	}

	public long getStatusUpdateTimeMillis() {
		return statusUpdateTimeMillis;
	}

	public long getStartTimeMillis() {
		return startTimeMillis;
	}

	public void setStartTimeMillis(long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}

	public long getEndTimeMillis() {
		return endTimeMillis;
	}

	public void setEndTimeMillis(long endTimeMillis) {
		this.endTimeMillis = endTimeMillis;
	}

	public long getUsedTimeMillis() {
		if (endTimeMillis > 0) {
			return endTimeMillis - startTimeMillis;
		}

		return System.currentTimeMillis() - startTimeMillis;
	}

	public ExecResult getExecResult() {
		return execResult;
	}

	public void setExecResult(ExecResult execResult) {
		this.execResult = execResult;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

//	public TaskRuntimeStatusListener getTaskRuntimeStatusListener() {
//		return runtimeStatusListener;
//	}

	public void addTaskRuntimeStatusListener(TaskRuntimeStatusListener eventListener) {
		if (runtimeStatusListeners == null) {
			runtimeStatusListeners = new ArrayList<>();
		}
		this.runtimeStatusListeners.add(eventListener);
	}

	public Collection<TaskRuntimeStatusListener> getRuntimeStatusListeners() {
		return runtimeStatusListeners;
	}

	@Override
	public String toString() {
		return "TaskRuntimeStatus [status=" + status + ", startTimeMillis=" + startTimeMillis + ", endTimeMillis=" + endTimeMillis + ", execResult="
				+ execResult + ", meta=" + meta + "]";
	}

}
