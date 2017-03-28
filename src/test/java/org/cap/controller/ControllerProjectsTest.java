package org.cap.controller;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class ControllerProjectsTest extends TestCase {
	public Project p;
	@InjectMocks
	public Projects ps;
	@Mock
	ProjectRepoImplMongo prim;
	public String title;

	protected void setUp() throws Exception {
		super.setUp();
		String title = Math.random()+"ARP";
		ps = new Projects();
		p = new Project(title);
	}
	@Test
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testIsAnEmail_shouldBeTrue_whenEmailIsOK(){
		assertEquals(ps.IsAnEmail("hello@gmail.com"),true);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooShort(){
		assertEquals(ps.IsAnEmail("h@m.f"),false);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooLong(){
		assertEquals(ps.IsAnEmail("hellohellohellohellohello@emailadressprovier.unitedstates"),false);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailHasTwoAt(){
		assertEquals(ps.IsAnEmail("hello@gm@ail.com"),false);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailContainsNoPoint(){
		assertEquals(ps.IsAnEmail("hello@gmailcom"),false);
	}
	@Test
	public void testCheckTitleNotExisting_shouldBeFalse_whenAlreadyExist(){
		assertEquals(ps.checkTitleNotExisting("Gestion"),false);
	}
	@Test
	public void testCheckTitleNotExisting_shouldBeTrue_whenEmailNotExisting(){
		assertEquals(ps.checkTitleNotExisting("I'm not in"),true);
	}
	
	

}
