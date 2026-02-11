package com.amituofo.task.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.common.ex.ParseException;
import com.amituofo.common.kit.config.ConfigKeeper;
import com.amituofo.common.kit.config.Configuration;
import com.amituofo.common.kit.config.LocalConfigKeeper;
import com.amituofo.common.util.StringUtils;
import com.amituofo.task.TaskArchive;
import com.amituofo.task.TaskDetail;
import com.amituofo.task.ex.ArchiveException;

public class LocalFileSystemTaskArchiveImpl implements TaskArchive {
	private final Map<String, TaskDetail> taskMap = new LinkedHashMap<String, TaskDetail>();
	private final ConfigKeeper<Configuration> cs;

	public LocalFileSystemTaskArchiveImpl(String configLocation, String ext, ClassLoader defaultClassLoader) throws ParseException, IOException {
		cs = new LocalConfigKeeper<Configuration>(configLocation, ext, null, defaultClassLoader);
		cs.reload(null);
		List<Configuration> confs = cs.list();
		for (Configuration conf : confs) {
			taskMap.put(conf.getId(), TaskDetail.parse(conf));
		}
	}

	@Override
	public void save(TaskDetail td) throws ArchiveException {
		try {
			cs.save(td, true);
			taskMap.put(td.getId(), td);
		} catch (Exception e) {
			throw new ArchiveException(e);
		}
	}

	@Override
	public TaskDetail get(String id) {
		return taskMap.get(id);
	}

	@Override
	public boolean delete(String id) throws ArchiveException {
		try {
			cs.delete(id);
		} catch (Exception e) {
			throw new ArchiveException(e);
		}
		taskMap.remove(id);
		return true;
	}

	@Override
	public Collection<TaskDetail> list() {
		return taskMap.values();
	}

	@Override
	public int count() {
		return taskMap.size();
	}

	@Override
	public Collection<TaskDetail> list(String groupId, String catalog) {
		List<TaskDetail> gettasks = new ArrayList<TaskDetail>();
		Collection<TaskDetail> tasks = list();

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
	public void deleteAll() throws ArchiveException {
		try {
			cs.deleteAll();
		} catch (Exception e) {
			throw new ArchiveException(e);
		}
		taskMap.clear();
	}

	@Override
	public boolean has(String id) {
		return taskMap.containsKey(id);
	}

}
