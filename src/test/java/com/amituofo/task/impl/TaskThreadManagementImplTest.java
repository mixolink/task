package com.amituofo.task.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class TaskThreadManagementImplTest {

	@Test
	void resizesPoolInBothDirections() {
		TaskThreadManagementImpl management = new TaskThreadManagementImpl(3);
		try {
			assertDoesNotThrow(() -> management.setTaskPoolSize(10));
			assertDoesNotThrow(() -> management.setTaskPoolSize(2));
		} finally {
			management.shutdown();
		}
	}
}
