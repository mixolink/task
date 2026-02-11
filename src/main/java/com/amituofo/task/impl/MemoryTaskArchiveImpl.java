package com.amituofo.task.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.util.StringUtils;
import com.amituofo.task.TaskArchive;
import com.amituofo.task.TaskDetail;

public class MemoryTaskArchiveImpl implements TaskArchive {
	private final List<TaskDetail> taskList = new ArrayList<>();
	private final Map<String, TaskDetail> taskMap = new HashMap<>();

	public MemoryTaskArchiveImpl() {
	}

	@Override
	public void save(TaskDetail td) {
		if (td == null) {
			return;
		}

		if (!taskMap.containsKey(td.getId())) {
			taskList.add(0, td);
			taskMap.put(td.getId(), td);
		}
	}

	@Override
	public TaskDetail get(String id) {
//		List<TaskDetail> tasks = list();
//
//		for (TaskDetail taskDetail : tasks) {
//			if (taskDetail.getId().equals(id)) {
//				return taskDetail;
//			}
//		}
//
//		return null;

		return taskMap.get(id);
	}

	@Override
	public boolean delete(String id) {
		TaskDetail task = taskMap.remove(id);
		if (task != null) {
			return taskList.remove(task);
		}

		return false;
	}

	@Override
	public List<TaskDetail> list() {
		return taskList;
	}

	@Override
	public int count() {
		return taskList.size();
	}

	@Override
	public List<TaskDetail> list(String groupId, String catalog) {
		List<TaskDetail> gettasks = new ArrayList<TaskDetail>();
		List<TaskDetail> tasks = list();

		for (TaskDetail taskDetail : tasks) {
			if (StringUtils.isNotEmpty(groupId) && StringUtils.isNotEmpty(taskDetail.getGroupId())) {
				if (!taskDetail.getGroupId().equalsIgnoreCase(groupId)) {
					continue;
				}
			}
			if (StringUtils.isEmpty(catalog)) {
				gettasks.add(taskDetail);
			} else if (StringUtils.isNotEmpty(taskDetail.getCatalog()) && taskDetail.getCatalog().toLowerCase().contains(catalog.toLowerCase())) {
				gettasks.add(taskDetail);
			}
		}

		return gettasks;
	}

	@Override
	public void deleteAll() {
		taskList.clear();
	}

	@Override
	public boolean has(String id) {
		return taskMap.containsKey(id);
	}

}
