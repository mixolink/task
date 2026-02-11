package com.amituofo.cmdlet.component;

import java.rmi.Naming;

import com.amituofo.cmdlet.core.Command;
import com.amituofo.task.ProcessServicePoint;


public abstract class TaskCommand<T extends TaskCommandArguments> extends Command<T> {
	private ProcessServicePoint processServicePoint = null;

	public TaskCommand(Class<T> commandArguments) {
		super(commandArguments);
	}

	protected ProcessServicePoint getProcessServicePoint() {
		if (processServicePoint != null) {
			return processServicePoint;
		}

		String host = cmdArg.getProcessServiceHost();
		String servicePoint = "rmi://" + host + "/" + ProcessServicePoint.class.getSimpleName();
		try {
			processServicePoint = (ProcessServicePoint) Naming.lookup(servicePoint);
		} catch (Exception e) {
			processServicePoint = null;
			e.printStackTrace();
		}
		return processServicePoint;
	}
}
