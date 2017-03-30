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
	public void testConstructor_shouldBeEqualToTitle_whenCall() {
		assertEquals(p.title, title);

	}
	@Test
	public void testConstructor_shouldntBeNull_whenCall() {
		assertTrue(p.id != null);
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
		assertEquals(p.id,p.getId());
	}
	@Test
	public void testSetUUID_shouldBeEqualToTheUUID_whenSet(){
		String id = UUID.randomUUID().toString();
		p.setId(id);
		assertEquals(id,p.id);
	}
	@Test
	public void testAddToList_shouldListBeInstanciatied_whenNull(){
		p.setMails(null);
		p.getMails().add("hello@test.com");
		assertTrue(p.mails != null);
	}
	@Test
	public void testAddToList_shouldBeAdded_whenCall(){
		String mail = "hellotest@test.com";
		p.setMails(null);
		p.getMails().add(mail);
		assertTrue(p.getMails().contains(mail));
	}
	

}
