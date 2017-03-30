package org.cap.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest extends TestCase{
	
	@InjectMocks
	ProjectService pServ;
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
		assertEquals(pServ.IsAnEmail("hello@gmail.com"),true);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooShort(){
		assertEquals(pServ.IsAnEmail("h@m.f"),false);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooLong(){
		assertEquals(pServ.IsAnEmail("hellohellohellohellohello@emailadressprovier.unitedstates"),false);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailHasTwoAt(){
		assertEquals(pServ.IsAnEmail("hello@gm@ail.com"),false);
	}
	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailContainsNoPoint(){
		assertEquals(pServ.IsAnEmail("hello@gmailcom"),false);
	}

	
	@Test
	public void testCheckTitleNotExisting_shouldBeFalse_whenAlreadyExist(){
		when(prim.getObjectByTitle("Gestion")).thenReturn(new Project("Gestion"));
		assertEquals(pServ.checkTitleNotExisting("Gestion"),false);
		verify(prim).getObjectByTitle("Gestion");
	}
	@Test
	public void testCheckTitleNotExisting_shouldBeTrue_whenEmailNotExisting(){
		when(prim.getObjectByTitle("I'm not in")).thenReturn(null);
		assertEquals(pServ.checkTitleNotExisting("I'm not in"),true);
		verify(prim).getObjectByTitle("I'm not in");
	}
	@Test(expected=EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenTitleNull(){
		pServ.saveProjects(null);
	}
	@Test(expected=EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenProjectDoesntExist(){
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		when(prim.getObject(uuid)).thenReturn(null);
		pServ.addEmail("hello@gmail.com", uuid);
		verify(prim).getObject(uuid);
	}

	@Test(expected=EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenEmailIsNull(){
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		when(prim.getObject(uuid)).thenReturn(proj);
		pServ.addEmail(null, uuid);
		verify(prim).getObject(uuid);
	}
	@Test(expected=EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenEmailIncorrect(){
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		when(prim.getObject(uuid)).thenReturn(proj);
		pServ.addEmail("a@a.a", uuid);
		verify(prim).getObject(uuid);
	}


}
