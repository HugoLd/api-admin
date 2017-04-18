package org.cap.controller.bean;

import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorOutputTest {
	ErrorOutput eop;

	@Test
	public void testEmptyConstructor_shouldAllFieldsBeNull_whenConstruct() {
		eop = new ErrorOutput();
		assertEquals(eop.getMessage(), null);
	}

	@Test
	public void testFullConstructor_shouldAllFieldsNotBeNull_whenConstruct() {
		eop = new ErrorOutput("Error occured");
		assertEquals(eop.getMessage(), "Error occured");
	}

	@Test
	public void testSetUuid_shouldBeEqual_whenSet() {
		eop = new ErrorOutput();
		eop.setMessage("Error occured");
		assertEquals(eop.getMessage(), "Error occured");
	}

}
