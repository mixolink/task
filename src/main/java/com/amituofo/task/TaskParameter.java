package com.amituofo.task;

import java.util.Map;

import com.amituofo.common.kit.value.Parameter;

public class TaskParameter extends Parameter {

	public TaskParameter() {
		super();
	}

	public TaskParameter(Map<String, Object> configMap) {
		super(configMap);
	}

	public TaskParameter(Parameter param) {
		super();
		super.resetTo(param);
	}

}
