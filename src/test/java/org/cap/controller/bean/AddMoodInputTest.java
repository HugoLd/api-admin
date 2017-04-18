package org.cap.controller.bean;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddMoodInputTest {
	AddMoodInput adm;
	
	@Test
	public void testEmptyConstructor_shouldAllFieldsBeNull_whenConstruct() {
		adm = new AddMoodInput();
		assertEquals(adm.getUuid(), null);
		assertEquals(adm.getUuidProj(), null);
		assertEquals(adm.getComment(), null);
		assertEquals(adm.getDate(), null);
	}
	
	@Test
	public void testFullConstructor_shouldAllFieldsNotBeNull_whenConstruct() {
		adm = new AddMoodInput("826393-fa492b9743-a982",2,"pas de commentaire","12-12-2012");
		assertEquals(adm.getUuid(), "826393-fa492b9743-a982");
		assertEquals(adm.getMood(), 2);
		assertEquals(adm.getComment(), "pas de commentaire");
		assertEquals(adm.getDate(), "12-12-2012");
	}
	
	@Test
	public void testSetUuid_shouldBeEqual_whenSet() {
		adm = new AddMoodInput();
		adm.setUuid("826393-fa492b9743-a982");		
		assertEquals(adm.getUuid(), "826393-fa492b9743-a982");
	}
	@Test
	public void testSetUuidProj_shouldBeEqual_whenSet() {
		adm = new AddMoodInput();
		adm.setUuidProj("8abc-f68649743-ac68");		
		assertEquals(adm.getUuidProj(), "8abc-f68649743-ac68");	
	}
	@Test
	public void testSetMood_shouldBeEqual_whenSet() {
		adm = new AddMoodInput();
		adm.setMood(3);
		assertEquals(adm.getMood(), 3);
	}
	@Test
	public void testSetComment_shouldBeEqual_whenSet() {
		adm = new AddMoodInput();
		adm.setComment("pas de commentaire");
		assertEquals(adm.getComment(), "pas de commentaire");
	}
	@Test
	public void testSetDate_shouldBeEqual_whenSet() {
		adm = new AddMoodInput();
		adm.setDate("12-12-2012");
		assertEquals(adm.getDate(), "12-12-2012");
	}

}
