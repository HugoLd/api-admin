package org.cap.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import freemarker.template.TemplateException;
import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest extends TestCase {

	@InjectMocks
	ProjectService pServ;
	@Mock
	ProjectRepoImplMongo prim;
	@Mock
	MailService mailService;

	private static final Project A_PROJECT = new Project();
	private static final String A_PROJECT_ID = "xxx-yyy";
	private static final String A_PROJECT_NAME = "yo";
	
	@Before
	public void setUp() throws Exception {
		A_PROJECT.setId(A_PROJECT_ID);
		A_PROJECT.setTitle(A_PROJECT_NAME);
		A_PROJECT.getMails().add("hlld@hotmail.fr");
	}

	@Test
	public void testIsAnEmail_shouldBeTrue_whenEmailIsOK() {
		assertEquals(pServ.isAnEmail("hello@gmail.com"), true);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooShort() {
		assertEquals(pServ.isAnEmail("h@m.f"), false);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooLong() {
		assertEquals(pServ.isAnEmail("hellohellohellohellohello@emailadressprovier.unitedstates"), false);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailHasTwoAt() {
		assertEquals(pServ.isAnEmail("hello@gm@ail.com"), false);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailContainsNoPoint() {
		assertEquals(pServ.isAnEmail("hello@gmailcom"), false);
	}

	@Test
	public void testIsTitleAlreadyExisting_shouldBeTrue_whenAlreadyExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(A_PROJECT);
		assertEquals(pServ.isTitleAlreadyExists(A_PROJECT_NAME), true);
		verify(prim).getObjectByTitle(A_PROJECT_NAME);
	}

	@Test
	public void testIsTitleAlreadyExisting_shouldBeFalse_whenNotExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(null);
		assertEquals(pServ.isTitleAlreadyExists(A_PROJECT_NAME), false);
		verify(prim).getObjectByTitle(A_PROJECT_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveProject_shouldBeAnIAException_whenJsonNull() {

		pServ.saveProject(null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveProject_shouldBeAnIAException_whenTitleNull() {

		pServ.saveProject(new Project());

	}
	@Test(expected = IllegalArgumentException.class)
	public void testSaveProject_shouldBeAnIAException_whenTitleNotInPattern() {
		Project p = new Project();
		p.setTitle("\t");
		pServ.saveProject(p);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveProject_shouldBeAnIAException_whenTitleAlreadyExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(new Project());
		pServ.saveProject(A_PROJECT);
		verify(prim).getObjectByTitle(A_PROJECT_NAME);
	}

	@Test
	public void testSaveProject_shouldBeAProject_whenTitleNotExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(null);
		assertEquals(pServ.saveProject(A_PROJECT).getClass(),Project.class);
		verify(prim).getObjectByTitle(A_PROJECT_NAME);
	}
	


	@Test(expected = IllegalArgumentException.class)
	public void testAddProject_shouldBeAnIAException_whenTitleNull() {

		pServ.addProject(null);

	}
	@Test(expected = IllegalArgumentException.class)
	public void testAddProject_shouldBeAnIAException_whenTitleNotInPattern() {
		
		pServ.addProject("\t");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddProject_shouldBeAnIAException_whenTitleAlreadyExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(new Project());
		pServ.addProject(A_PROJECT_NAME);
	}

	@Test
	public void testAddProject_shouldBeAProject_whenTitleNotExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(null);
		assertEquals(pServ.addProject(A_PROJECT_NAME).getClass(),Project.class);
		verify(prim).getObjectByTitle(A_PROJECT_NAME);
	}
	
	

	@Test(expected = IllegalArgumentException.class)
	public void testAddUserToProject_shouldBeAnIAException_whenEmailNotCorrect() {
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		pServ.addUserToProject(uuid,"kfj.fr");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testAddUserToProject_shouldBeAnIAException_whenProjectNull() {
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		pServ.addUserToProject(uuid,"hello@gmail.com");
	}	

	@Test
	public void testAddUserToProject_shouldReturnAProject_whenEmailIsAlreadyIn()
			throws JsonParseException, JsonMappingException, IOException {
		when(prim.get(A_PROJECT_ID)).thenReturn(A_PROJECT);
		pServ.addUserToProject(A_PROJECT_ID, "hlld@hotmail.fr");
		verify(prim).get(A_PROJECT_ID);
	}


	@Test
	public void testAddUserToProject_shouldAProject_whenEmailNotIn() {
		when(prim.get(A_PROJECT_ID)).thenReturn(A_PROJECT);
		pServ.addUserToProject(A_PROJECT_ID, "hello@mail.com");
		verify(prim).get(A_PROJECT_ID);
	}


	@Test
	public void testGetProjects_shouldNotBeNull_whenProjectIn() {
		List<Project> lp = new ArrayList<Project>();
		lp.add(A_PROJECT);
		when(prim.getAll()).thenReturn(lp);
		assertTrue(pServ.getProjects() != null);
		verify(prim).getAll();
	}

	@Test
	public void testGetProjects_shouldBeNull_whenNoProjectIn() {
		when(prim.getAll()).thenReturn(null);
		assertTrue(pServ.getProjects() == null);
		verify(prim).getAll();
	}

	@Test
	public void testGetProject_shouldNotBeNull_whenProjectExist() {
		when(prim.get(A_PROJECT_ID)).thenReturn(A_PROJECT);
		assertTrue(pServ.getProject(A_PROJECT_ID) != null);
		verify(prim).get(A_PROJECT_ID);
	}
	@Test
	public void testGetProject_shouldBeNull_whenProjectNotExist() {
		when(prim.get(A_PROJECT_ID)).thenReturn(null);
		assertEquals(pServ.getProject(A_PROJECT_ID) , null);
		verify(prim).get(A_PROJECT_ID);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSendMail_shouldBeAnIAException_whenProjectNull() throws IOException, TemplateException {		
		
		when(prim.get(A_PROJECT_ID)).thenReturn(null);
		pServ.sendMail(A_PROJECT_ID);
		verify(prim).get(A_PROJECT_ID);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSendMail_shouldBeAnIAException_whenPropsNotOk() throws IOException, TemplateException {		
		when(mailService.checkProperties()).thenReturn(false);
		when(prim.get(A_PROJECT_ID)).thenReturn(A_PROJECT);
		pServ.sendMail(A_PROJECT_ID);
		verify(prim).get(A_PROJECT_ID);
		verify(mailService).checkProperties();
	}
	
	@Test
	public void testSendMail_shouldPass_whenOk() throws IOException, TemplateException {
		when(mailService.checkProperties()).thenReturn(true);
		when(prim.get(A_PROJECT_ID)).thenReturn(A_PROJECT);
		pServ.sendMail(A_PROJECT_ID);
		verify(prim).get(A_PROJECT_ID);
		verify(mailService).checkProperties();
	}
	
	@Test
	public void testSendMailAll_shouldCallSend2Times_whenListLength2() throws IOException, TemplateException {
		List<Project> lp = new ArrayList<Project>();
		lp.add(new Project());		
		lp.add(new Project());
		lp.get(0).setTitle("title");
		lp.get(0).setId(A_PROJECT_ID);
		lp.get(1).setTitle("test");
		lp.get(1).setId("idproj2");
		when(mailService.checkProperties()).thenReturn(true);
		when(prim.getAll()).thenReturn(lp);
		when(prim.get(lp.get(0).getId())).thenReturn(A_PROJECT);
		when(prim.get(lp.get(1).getId())).thenReturn(A_PROJECT);
		pServ.sendMailAll();
		verify(prim).get(A_PROJECT_ID);
		verify(prim).get("idproj2");
		verify(prim).get(A_PROJECT_ID);
		verify(mailService,times(2)).checkProperties();
	}
	
	
	@Test
	public void testDeleteProj_shouldNotThrowEx_whenCalled(){	
		pServ.deleteProj(A_PROJECT_ID);
		verify(prim).delete(A_PROJECT_ID);		
	}
	@Test
	public void testDeleteUser_shouldNotThrowEx_whenCalled(){	
		pServ.deleteUser("hello@mail.com" ,A_PROJECT_ID);
		verify(prim).deleteUser("hello@mail.com",A_PROJECT_ID);		
	}
}

