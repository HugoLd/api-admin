package org.cap.controller.bean;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddUserToProjectInputTest {
	AddUserToProjectInput aupm;

	@Test
	public void testEmptyConstructor_shouldAllFieldsBeNull_whenConstruct() {
		aupm = new AddUserToProjectInput();
		assertEquals(aupm.getEmail(), null);
	}

	@Test
	public void testSetTitleDate_shouldBeEqual_whenSet() {
		aupm = new AddUserToProjectInput();
		aupm.setEmail("tester@test.test");
		assertEquals(aupm.getEmail(), "tester@test.test");
	}
}
