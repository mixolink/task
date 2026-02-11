package com.amituofo.task;

import com.amituofo.common.kit.config.Configuration;

public class TaskDetail extends Configuration {
//	private final SimpleConfiguration conf = new SimpleConfiguration();
	private final Configuration conf = this;

	public static final String TASK_CLASS_NAME = "_TASK_CLASS_NAME_";
	public static final String TASK_PARAMETER = "_TASK_PARAMETER_";
	public static final String TASK_CATALOG = "_TASK_CATALOG_";
	public static final String TASK_GROUPID = "_TASK_GROUPID_";
	public static final String TASK_WORKINGFOLDER = "_TASK_WORKINGFOLDER_";
	public static final String TASK_OWNER = "_TASK_OWNER_";
	public static final String TASK_DISABLED = "_TASK_DISABLED_";
	public static final String TASK_SCHEDULE_EXPRESSION = "_TASK_SCHEDULE_EXPRESSION_";

	public TaskDetail() {
	}

	public TaskDetail(String name, Class<? extends Task> taskClass) {
		this(null, name, taskClass);
	}

	public TaskDetail(String id, String name, Class<? extends Task> taskClass) {
		if (id == null) {
			conf.generateNewID();
		} else {
			conf.setId(id);
		}
		conf.setName(name);
		conf.set(TASK_CLASS_NAME, taskClass);
		conf.set(TASK_PARAMETER, new TaskParameter());
	}

	public TaskDetail(Class<? extends Task> taskClass) {
		this(null, taskClass);
	}

	public boolean equals(TaskDetail obj) {
//		return conf.getId().equals(obj.getId());
		return conf.equals(obj.conf);
	}

//	public String getId() {
//		return conf.getId();
//	}
//
//	public String getName() {
//		return conf.getName();
//	}
//
//	public void setName(String name) {
//		conf.setName(name);
//	}

	public Object getAttribute(String name) {
		return conf.getObject(name);
	}

	public void setAttribute(String name, Object value) {
		conf.set(name, value);
	}

	public boolean isDisabled() {
		return conf.getBoolean(TASK_DISABLED, false);
	}

	public void setDisabled(boolean disable) {
		conf.set(TASK_DISABLED, disable);
	}

	public String getCatalog() {
		return conf.getString(TASK_CATALOG);
	}

	public void setCatalog(String catalog) {
		conf.set(TASK_CATALOG, catalog);
	}

	public String getGroupId() {
		return conf.getString(TASK_GROUPID);
	}

	public void setGroupId(String groupId) {
		conf.set(TASK_GROUPID, groupId);
	}

	public String getWorkingFolder() {
		return conf.getString(TASK_WORKINGFOLDER);
	}

	public void setWorkingFolder(String workingFolder) {
		conf.set(TASK_WORKINGFOLDER, workingFolder);
	}

	public String getOwner() {
		return conf.getString(TASK_OWNER);
	}

	public void setOwner(String owner) {
		conf.set(TASK_OWNER, owner);
	}

	public String getScheduleExpression() {
		return conf.getString(TASK_SCHEDULE_EXPRESSION);
	}

	public void setScheduleExpression(String scheduleExpression) {
		conf.set(TASK_SCHEDULE_EXPRESSION, scheduleExpression);
	}

	public TaskParameter getParameter() {
		try {
			return (TaskParameter) conf.buildConfigurationMapObject(TASK_PARAMETER, TaskParameter.class);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setParameter(TaskParameter parameter) {
		conf.set(TASK_PARAMETER, parameter);
	}

	public Class<? extends TaskBase> getTaskClass() throws ClassNotFoundException {
		Object o = conf.getObject(TASK_CLASS_NAME);
		if (o != null) {
			if (o instanceof String) {
				Class<? extends TaskBase> c = (Class<? extends TaskBase>) Class.forName((String) o);
				setTaskClass(c);
				return c;
			}
		}

		return (Class<? extends TaskBase>) conf.getObject(TASK_CLASS_NAME);
	}

	public void setTaskClass(Class<? extends TaskBase> taskClass) {
		conf.set(TASK_CLASS_NAME, taskClass);
	}

//	public String getDescription() {
//		return conf.getDescription();
//	}
//
//	public void setDescription(String description) {
//		conf.setDescription(description);
//	}

//	public ClassicConfiguration toSimpleConfiguration() {
//		return conf;
//	}

	public static TaskDetail parse(Configuration simpleConfiguration) {
		TaskDetail td = new TaskDetail(null, null);
		td.conf.resetTo(simpleConfiguration);
		return td;
	}

}
