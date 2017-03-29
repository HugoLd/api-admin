package org.cap.bean;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ProjectTest extends TestCase {

	public Project p;
	public String title;
	@Before
	protected void setUp() throws Exception {
		title = "Management";
		p = new Project(title);
		super.setUp();

	}
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testConstructor_shouldBeEqualToTitle_whenCallConstructor() {
		assertEquals(p.title, title);

	}
	@Test
	public void testConstructor_shouldntBeNull_whenCall() {
		assertTrue(p._id != null);
	}
	@Test
	public void testGetTitle_shouldBeEqualToTheTitle_whenCall() {
		assertEquals(p.getTitle(),p.title);
	}
	@Test
	public void testSetTitle_shouldBeEqualTothe_whenSet() {
		String tit = "testSetTitle";
		p.setTitle(tit);
		assertEquals(p.title, tit);
	}
	@Test
	public void testGetUUID_shouldBeEqualToTheUUID_WhenCall(){
		assertEquals(p._id,p.get_id());
	}
	@Test
	public void testSetUUID_shouldBeEqualToTheUUID_whenSet(){
		String id = UUID.randomUUID().toString();
		p.set_id(id);
		assertEquals(id,p._id);
	}

}
