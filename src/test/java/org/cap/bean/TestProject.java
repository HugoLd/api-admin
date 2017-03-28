package org.cap.bean;

import java.util.UUID;

import junit.framework.TestCase;

public class TestProject extends TestCase {

	public Project p;
	public String title;

	protected void setUp() throws Exception {
		title = "Management";
		p = new Project(title);
		super.setUp();

	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConstructorTitle() {
		assertEquals(p.title, title);

	}

	public void testConstructorUUID() {
		assertTrue(p._id != null);
	}

	public void testGetTitle() {
		assertEquals(p.getTitle(),p.title);
	}

	public void testSetTitle() {
		String tit = "testSetTitle";
		p.setTitle(tit);
		assertEquals(p.title, tit);
	}
	public void testGetUUID(){
		assertEquals(p._id,p.get_id());
	}
	public void testSetUUID(){
		UUID id = UUID.randomUUID();
		p.set_id(id);
		assertEquals(id,p._id);
	}

}
