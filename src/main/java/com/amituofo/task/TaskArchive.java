package com.amituofo.task;

import java.util.Collection;

import com.amituofo.task.ex.ArchiveException;

public interface TaskArchive {

	void save(TaskDetail td) throws ArchiveException;

	TaskDetail get(String id) throws ArchiveException;

	boolean delete(String id) throws ArchiveException;

	Collection<TaskDetail> list() throws ArchiveException;

	Collection<TaskDetail> list(String groupId, String catalog) throws ArchiveException;

	void deleteAll() throws ArchiveException;

	int count() throws ArchiveException;

	boolean has(String id) throws ArchiveException;
}
