package org.cap.bean;

import java.util.UUID;

import org.junit.Test;

import junit.framework.TestCase;

public class TestProject extends TestCase {

	public Project p;
	public String title;

	protected void setUp() throws Exception {
		title = "Management";
		p = new Project(title);
		super.setUp();

	}
	@Test
	public void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testConstructorTitle() {
		assertEquals(p.title, title);

	}
	@Test
	public void testConstructorUUID() {
		assertTrue(p._id != null);
	}
	@Test
	public void testGetTitle() {
		assertEquals(p.getTitle(),p.title);
	}
	@Test
	public void testSetTitle() {
		String tit = "testSetTitle";
		p.setTitle(tit);
		assertEquals(p.title, tit);
	}
	@Test
	public void testGetUUID(){
		assertEquals(p._id,p.get_id());
	}
	@Test
	public void testSetUUID(){
		UUID id = UUID.randomUUID();
		p.set_id(id);
		assertEquals(id,p._id);
	}

}
