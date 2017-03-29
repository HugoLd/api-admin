package org.cap.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class ControllerProjectsTest extends TestCase {
	@InjectMocks
	public Projects ps;
	@Mock
	ProjectRepoImplMongo prim;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	@After
	public void tearDown() throws Exception {
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
		when(prim.getObjectByTitle("Gestion")).thenReturn(new Project("Gestion"));
		assertEquals(ps.checkTitleNotExisting("Gestion"),false);
		verify(prim).getObjectByTitle("Gestion");
	}
	@Test
	public void testCheckTitleNotExisting_shouldBeTrue_whenEmailNotExisting(){
		when(prim.getObjectByTitle("I'm not in")).thenReturn(null);
		assertEquals(ps.checkTitleNotExisting("I'm not in"),true);
		verify(prim).getObjectByTitle("I'm not in");
	}
	@Test
	public void testSaveProject_shouldBeAProject_whenprojectSaved(){
		Project proj = ps.saveProjects("hello");
		assertEquals(proj.getClass(),Project.class);
		
	}
	@Test(expected=EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenTitleNull(){
		ps.saveProjects(null);
	}
	@Test(expected=EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenAlreadyExist(){
		when(prim.getObjectByTitle("hello")).thenReturn(new Project("hello"));
		ps.saveProjects("hello");
		verify(prim).getObjectByTitle("hello");
	}

}
