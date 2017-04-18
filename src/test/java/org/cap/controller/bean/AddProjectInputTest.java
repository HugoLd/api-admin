package org.cap.controller.bean;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddProjectInputTest {

	AddProjectInput apm;

	@Test
	public void testEmptyConstructor_shouldAllFieldsBeNull_whenConstruct() {
		apm = new AddProjectInput();
		assertEquals(apm.getTitle(), null);
	}

	@Test
	public void testSetTitleDate_shouldBeEqual_whenSet() {
		apm = new AddProjectInput();
		apm.setTitle("Project title");
		assertEquals(apm.getTitle(), "Project title");
	}
}
