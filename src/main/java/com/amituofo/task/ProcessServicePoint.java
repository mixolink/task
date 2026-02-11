package com.amituofo.task;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.amituofo.common.type.RunStatus;
import com.amituofo.task.impl.TaskRuntimeMessage;
import com.amituofo.task.impl.TaskRuntimePerformance;
import com.amituofo.task.impl.TaskRuntimeProgress;

public interface ProcessServicePoint extends Remote {
	void updatePerformance(String id, TaskRuntimePerformance performance) throws RemoteException;;

	void updateMessage(String id, TaskRuntimeMessage message) throws RemoteException;;

	void updateStatus(String id, RunStatus status, TaskRuntimeStatus details) throws RemoteException;;

	void updateProgress(String id, TaskRuntimeProgress details) throws RemoteException;;

}
