package com.amituofo.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amituofo.common.type.RunStatus;
import com.amituofo.task.impl.TaskRuntimeMessage;
import com.amituofo.task.impl.TaskRuntimePerformance;
import com.amituofo.task.impl.TaskRuntimeProgress;

//public class TaskExecuter implements Runnable {
public class TaskThread extends Thread {
	protected Logger logger = LogManager.getLogger(Task.class);

	private final TaskDetail taskDetail;
	private final TaskRuntimeStatus taskRuntimeStatus;
	private final TaskRuntimeProgress taskRuntimeProgress;
	private final TaskRuntimeMessage taskRuntimeMessage;
	private final TaskRuntimePerformance taskRuntimePerformance;

	private TaskRuntimeProgressListener progressListener;
	private TaskRuntimeMessageListener runtimeMessageListener;
	private TaskRuntimePerformanceListener performanceListener;
	private List<TaskStopListener> stopListenerListeners = null;

	private TaskBase taskInstance = null;

	private final String LOG_HEAD1;
	private final String LOG_HEAD2;

	public TaskThread(TaskDetail taskDetail) {
		this(taskDetail, new TaskRuntimeStatus(), new TaskRuntimeProgress(), new TaskRuntimeMessage(), new TaskRuntimePerformance());
	}

	public TaskThread(TaskDetail taskDetail, TaskRuntimeStatus taskRuntimeStatus, TaskRuntimeProgress taskRuntimeProgress, TaskRuntimeMessage taskRuntimeMessage,
			TaskRuntimePerformance taskRuntimePerformance) {
		this.taskDetail = taskDetail;
		this.taskRuntimeStatus = taskRuntimeStatus;
		this.taskRuntimeProgress = taskRuntimeProgress;
		this.taskRuntimeMessage = taskRuntimeMessage;
		this.taskRuntimePerformance = taskRuntimePerformance;

		LOG_HEAD1 = "Task-Name:[" + taskDetail.getName() + "] Catalog:[" + taskDetail.getCatalog() + "] ID:[" + taskDetail.getId() + "] ";
		LOG_HEAD2 = "Task-[" + taskDetail.getId() + "] ";

		this.setProgressListener(null);
		this.setRuntimeMessageListener(null);
		this.setPerformanceListener(null);
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getTaskId() {
		return taskDetail.getId();
	}

	public TaskDetail getTaskDetails() {
		return taskDetail;
	}

	public TaskRuntimeStatus getRuntimeStatus() {
		return taskRuntimeStatus;
	}

	public TaskRuntimeProgress getRuntimeProgress() {
		return taskRuntimeProgress;
	}

	public TaskRuntimeMessage getRuntimeMessage() {
		return taskRuntimeMessage;
	}

	public TaskRuntimeProgressListener getProgressListener() {
		return progressListener;
	}

	public TaskRuntimeMessageListener getRuntimeMessageListener() {
		return runtimeMessageListener;
	}

	public TaskRuntimePerformanceListener getPerformanceListener() {
		return performanceListener;
	}

	public Collection<TaskRuntimeStatusListener> getRuntimeStatusListeners() {
		return taskRuntimeStatus.getRuntimeStatusListeners();
	}

	public TaskRuntimePerformance getRuntimePerformance() {
		return taskRuntimePerformance;
	}

	public void setEventListener(final TaskRuntimeStatusListener eventListener) {
		if (eventListener == null) {
			this.taskRuntimeStatus.addTaskRuntimeStatusListener(TaskRuntimeStatusListener.NOOP);
		} else {
			this.taskRuntimeStatus.addTaskRuntimeStatusListener(eventListener);
		}
	}

	public TaskBase getTaskInstance() {
		return taskInstance;
	}

	public void addStopListener(TaskStopListener listener) {
		if (listener == null) {
			return;
		}

		if (stopListenerListeners == null) {
			stopListenerListeners = new ArrayList<>();
		}

		stopListenerListeners.add(listener);
	}

	public void setProgressListener(final TaskRuntimeProgressListener progressListener) {
		if (progressListener == null) {
			this.progressListener = new TaskRuntimeProgressListener() {

				@Override
				public void updateProgressTo100Percent() {
					taskRuntimeProgress.updateProgressTo100Percent();
				}

				@Override
				public void updateProgress(long progress) {
					taskRuntimeProgress.updateProgress(progress);
				}

				@Override
				public void updateProgressMode(ProgressMode progressMode) {
					taskRuntimeProgress.updateProgressMode(progressMode);
				}

				@Override
				public void setMaxProgress(long maxProgress) {
					taskRuntimeProgress.setMaxProgress(maxProgress);
				}
			};
		} else {
			this.progressListener = new TaskRuntimeProgressListener() {

				@Override
				public void updateProgressTo100Percent() {
					taskRuntimeProgress.updateProgressTo100Percent();
					progressListener.updateProgressTo100Percent();
				}

				@Override
				public void updateProgress(long progress) {
					taskRuntimeProgress.updateProgress(progress);
					progressListener.updateProgress(progress);
				}

				@Override
				public void updateProgressMode(ProgressMode progressMode) {
					taskRuntimeProgress.updateProgressMode(progressMode);
					progressListener.updateProgressMode(progressMode);
				}

				@Override
				public void setMaxProgress(long maxProgress) {
					taskRuntimeProgress.setMaxProgress(maxProgress);
					progressListener.setMaxProgress(maxProgress);
				}
			};
		}
	}

	public void setRuntimeMessageListener(final TaskRuntimeMessageListener runtimeMessageListener) {
		if (runtimeMessageListener == null) {
			this.runtimeMessageListener = new TaskRuntimeMessageListener() {

				@Override
				public void updateRuntimeMessage(String runtimeMessage) {
					taskRuntimeMessage.updateRuntimeMessage(runtimeMessage);
				}

				@Override
				public void updateAdditionMessage(String msg) {
					taskRuntimeMessage.updateAdditionMessage(msg);
				}

				@Override
				public void updateRuntimeName(String name) {
					taskRuntimeMessage.updateRuntimeName(name);
				}
			};
		} else {
			this.runtimeMessageListener = new TaskRuntimeMessageListener() {

				@Override
				public void updateRuntimeMessage(String runtimeMessage) {
					taskRuntimeMessage.updateRuntimeMessage(runtimeMessage);
					runtimeMessageListener.updateRuntimeMessage(runtimeMessage);
				}

				@Override
				public void updateAdditionMessage(String msg) {
					taskRuntimeMessage.updateAdditionMessage(msg);
					runtimeMessageListener.updateAdditionMessage(msg);
				}

				@Override
				public void updateRuntimeName(String name) {
					taskRuntimeMessage.updateRuntimeName(name);
					runtimeMessageListener.updateRuntimeName(name);
				}
			};
		}
	}

	public void setPerformanceListener(final TaskRuntimePerformanceListener performanceListener) {
		if (performanceListener == null) {
			this.performanceListener = new TaskRuntimePerformanceListener() {

				@Override
				public void updateWorkerCount(int workerSize) {
					taskRuntimePerformance.updateWorkerCount(workerSize);
				}

				@Override
				public void updatePerformance(Object performance) {
					taskRuntimePerformance.updatePerformance(performance);
				}

				@Override
				public void updateRemainSecond(int remainSecond) {
					taskRuntimePerformance.updateRemainSecond(remainSecond);
				}
			};
		} else {
			this.performanceListener = new TaskRuntimePerformanceListener() {

				@Override
				public void updateWorkerCount(int workerSize) {
					taskRuntimePerformance.updateWorkerCount(workerSize);
					performanceListener.updateWorkerCount(workerSize);
				}

				@Override
				public void updatePerformance(Object performance) {
					taskRuntimePerformance.updatePerformance(performance);
					performanceListener.updatePerformance(performance);
				}

				@Override
				public void updateRemainSecond(int remainSecond) {
					taskRuntimePerformance.updateRemainSecond(remainSecond);
				}
			};
		}
	}

	public boolean kill() {
		boolean killed = true;
		synchronized (taskRuntimeStatus) {
			if (taskInstance != null) {
				RunStatus status = taskRuntimeStatus.getStatus();

				if (status == RunStatus.Interrupting || status == RunStatus.Stopping || status == RunStatus.Stoped) {
					return true;
				}

				logger.info(LOG_HEAD2 + "Killing task thread... ");

				taskRuntimeStatus.setStatus(RunStatus.Interrupting, taskInstance.getStatusDescription(RunStatus.Interrupting));

				try {
					killed = taskInstance.kill();

					if (!killed) {
						// 任务执行结束
//						taskRuntimeStatus.setEndTimeMillis(System.currentTimeMillis());
//						taskRuntimeStatus.setStatus(RunStatus.Stoped, taskInstance.getStatusDescription(RunStatus.Stoped));
//						logger.info(LOG_HEAD2 + "Interrupted. ");
//					} else {
						// 潜在Bug 可能引起其他进程中断
//						super.interrupt();
//						logger.info(LOG_HEAD2 + "Interrupted. ");
//						taskRuntimeStatus.setStatus(status, "Unable to kill task thread " + taskInstance.getTaskId());
						logger.warn(LOG_HEAD2 + "Unable to kill task thread " + taskInstance.getTaskId());
						return false;
					}

					taskRuntimeStatus.setExecResult(ExecResult.Interrupted);
				} catch (Exception e) {
					logger.error(LOG_HEAD2 + "Error when killing task.", e);
					taskRuntimeStatus.setStatus(status, "Failed when trying to kill task thread " + taskInstance.getTaskId());
				} finally {
					logger.info(LOG_HEAD2 + "Task kill " + (killed ? "succeed." : "failure."));
				}
			} else {
				// 任务执行结束
				taskRuntimeStatus.setEndTimeMillis(System.currentTimeMillis());
				taskRuntimeStatus.setStatus(RunStatus.Stoped, null);
			}
		}

		return killed;
	}

	@Override
	public void interrupt() {
		boolean stoped = true;
		synchronized (taskRuntimeStatus) {
			if (taskInstance != null) {
				boolean needTryStop = false;
				RunStatus status = taskRuntimeStatus.getStatus();

				if (status == RunStatus.Interrupting || status == RunStatus.Stopping || status == RunStatus.Stoped) {
					return;
				}

				logger.info(LOG_HEAD2 + "Stopping task thread... ");

				if (status != RunStatus.Pending) {
					needTryStop = true;
				}

				taskRuntimeStatus.setStatus(RunStatus.Interrupting, taskInstance.getStatusDescription(RunStatus.Interrupting));

				try {
					if (needTryStop) {
						stoped = taskInstance.tryStop();
					} else {
						stoped = true;
					}

					if (!stoped) {
						// 任务执行结束
//						taskRuntimeStatus.setEndTimeMillis(System.currentTimeMillis());
//						taskRuntimeStatus.setStatus(RunStatus.Stoped, taskInstance.getStatusDescription(RunStatus.Stoped));
//						eventListener.statusChanged(TaskStatus.Canceled);
//						logger.info(LOG_HEAD2 + "stopped. ");
//					} else {
						// 潜在Bug 可能引起其他进程中断
//						super.interrupt();
						logger.warn(LOG_HEAD2 + "Unable to stop task thread " + taskInstance.getTaskId());
//						taskRuntimeStatus.setStatus(status, "Unable to stop task thread " + taskInstance.getTaskId());
						return;
					}

					taskRuntimeStatus.setExecResult(ExecResult.Interrupted);
				} catch (Exception e) {
					// e.printStackTrace();
					logger.error(LOG_HEAD2 + "Error when stopping task.", e);
					taskRuntimeStatus.setStatus(status, "Failed when trying to stop task thread " + taskInstance.getTaskId());
				} finally {
					logger.info(LOG_HEAD2 + "Task interrupt " + (stoped ? "succeed." : "failure."));
				}

			} else {
				// 任务执行结束
				taskRuntimeStatus.setEndTimeMillis(System.currentTimeMillis());
				taskRuntimeStatus.setStatus(RunStatus.Stoped, null);
//				eventListener.statusChanged(TaskStatus.Canceled);
			}
		}
	}

	@Override
	public void run() {
		RunStatus status = taskRuntimeStatus.getStatus();
		if (status.isRunningStatus()) {
			return;
		}

		taskRuntimeStatus.setStatus(RunStatus.Starting, null);
		ExecResult result = null;
		boolean initialized = false, prepared = false;
		try {
			// 创建任务实例
			logger.info(LOG_HEAD2 + "Creating instance of " + LOG_HEAD1);
			Class<? extends TaskBase> taskclazz = taskDetail.getTaskClass();
			if (taskclazz == null) {
				logger.error(LOG_HEAD2 + "Task class name not found. " + LOG_HEAD1);
			} else {
				taskInstance = taskclazz.newInstance();

				if (taskInstance == null) {
					logger.error(LOG_HEAD2 + "Unable to create task instance [" + taskDetail.getTaskClass().getName() + "]. " + LOG_HEAD1);
				} else {
					taskInstance.setTaskDetail(taskDetail);
					taskInstance.setRuntimeStatus(taskRuntimeStatus);
					// 配置进度
					taskInstance.setRuntimeProgressListener(progressListener);
					taskInstance.setRuntimePerformanceListener(performanceListener);
					taskInstance.setRuntimeMessageListener(runtimeMessageListener);
					taskInstance.setLogger(logger);

					TaskParameter parameter = taskDetail.getParameter();

					// 设置状态为初始化
					taskRuntimeStatus.setStatus(RunStatus.Initializing, taskInstance.getStatusDescription(RunStatus.Initializing));

					// 执行初始化
					logger.info(LOG_HEAD2 + "Initializing... ");

					// 设置启动时间
					taskRuntimeStatus.setStartTimeMillis(System.currentTimeMillis());
					initialized = taskInstance.initialize(parameter);
				}
			}

			if (initialized) {
				// 设置状态为准备中
//				taskRuntimeStatus.setStatus(RunStatus.Initializing, taskInstance.getStatusDescription(RunStatus.Initializing));
//				eventListener.statusChanged(TaskStatus.Preparing);

				// 执行任务
				logger.info(LOG_HEAD2 + "Preparing...");
				prepared = taskInstance.prepare();

				if (prepared) {
					// 设置状态为运行中
					taskRuntimeStatus.setStatus(RunStatus.Running, taskInstance.getStatusDescription(RunStatus.Running));

					result = null;

					// 执行任务
					logger.info(LOG_HEAD2 + "Start running...");
					taskInstance.execute();
					logger.info(LOG_HEAD2 + "End running.");
				} else {
					// 初始化失败
					logger.error(LOG_HEAD2 + "Prepare NG.");
					result = ExecResult.InitFailure;
				}
			} else {
				// 初始化失败
				logger.error(LOG_HEAD2 + "Initialize NG.");
				result = ExecResult.InitFailure;
			}
		} catch (Throwable e) {
			result = (prepared ? ExecResult.Failure : ExecResult.InitFailure);
			taskRuntimeStatus.setCause(e);

			logger.error(LOG_HEAD2 + "Catch exception when processing task.", e);
		} finally {
			synchronized (taskRuntimeStatus) {
				// 任务执行结束
				taskRuntimeStatus.setEndTimeMillis(System.currentTimeMillis());

				ExecResult finalResult = null;
				// 如果任务被执行了，执行finish清理结束
				if (taskInstance != null && initialized) {
					try {
						if (taskRuntimeStatus.getStatus().isRunningStatus()) {
							taskRuntimeStatus.setStatus(RunStatus.Stopping, taskInstance.getStatusDescription(RunStatus.Stopping));
						}

						logger.info(LOG_HEAD2 + "Finising...");
						finalResult = taskInstance.finish();
					} catch (Throwable e) {
						result = ExecResult.Failure;
						taskRuntimeStatus.setCause(e);
						logger.error(LOG_HEAD2 + "Catch exception when finishing task.", e);
					}
				}

				// 设置最终运行结果
				if (result == null) {
					result = (finalResult == null ? ExecResult.Success : finalResult);
				}

				logger.info(LOG_HEAD2 + "Finished with [" + result + "]");

				taskRuntimeStatus.setExecResult(result);
				taskRuntimeStatus.setStatus(RunStatus.Stoped, taskInstance != null ? taskInstance.getStatusDescription(RunStatus.Stoped) : null);

				if (taskInstance != null) {
					try {
						taskInstance.release();
						logger.info(LOG_HEAD2 + "Resource released.");
					} catch (Exception e) {
						logger.error(LOG_HEAD2 + "Catch exception when releasing resource.", e);
					}
				}

				if (stopListenerListeners != null) {
					for (TaskStopListener taskStopListener : stopListenerListeners) {
						taskStopListener.stopping();
					}
				}

				stopListenerListeners = null;
				logger.info(LOG_HEAD2 + "Task stopped.");
				taskInstance = null;
			}
//			taskDetail.getParameter().clear();
		}
	}

}
