package org.cap.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest extends TestCase {

	@InjectMocks
	ProjectService pServ;
	@Mock
	ProjectRepoImplMongo prim;
	@Mock
	MailService ms;

	private static final Project A_PROJECT = new Project();
	private static final String A_PROJECT_ID = "xxx-yyy";
	private static final String A_PROJECT_NAME = "yo";
	
	@Before
	public void setUp() throws Exception {
		A_PROJECT.setId(A_PROJECT_ID);
		A_PROJECT.setTitle(A_PROJECT_NAME);
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
	public void testCheckTitleNotExisting_shouldBeFalse_whenAlreadyExist() {
		when(prim.getObjectByTitle("Gestion")).thenReturn(A_PROJECT);
		assertEquals(pServ.checkTitleNotExisting(A_PROJECT_NAME), false);
		verify(prim).getObjectByTitle("Gestion");
	}

	@Test
	public void testCheckTitleNotExisting_shouldBeTrue_whenEmailNotExisting() {
		when(prim.getObjectByTitle("I'm not in")).thenReturn(null);
		assertEquals(pServ.checkTitleNotExisting("I'm not in"), true);
		verify(prim).getObjectByTitle("I'm not in");
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenJsonNull() {

		pServ.saveProject(null);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenTitleNull() {

		pServ.saveProject(new Project());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenTitleAlreadyExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(new Project());
		pServ.saveProject(A_PROJECT);
	}

	@Test
	public void testSaveProject_shouldBeAnERDAEException_whenTitleNotExist() {
		when(prim.getObjectByTitle(A_PROJECT_NAME)).thenReturn(null);
		pServ.saveProject(A_PROJECT);
		verify(prim).getObjectByTitle(A_PROJECT_NAME);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenProjectDoesntExist() {
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		when(prim.get(uuid)).thenReturn(null);
		pServ.addEmail("{\"email\":\"hello@gmail.com\"}", uuid);

		verify(prim).get(uuid);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenEmailIsNull()
			throws JsonParseException, JsonMappingException, IOException {
		when(prim.get(A_PROJECT_ID)).thenReturn(A_PROJECT);
		pServ.addEmail(null, uuid);

		verify(prim).get(uuid);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenEmailIncorrect()
			throws JsonParseException, JsonMappingException, IOException {
		A_PROJECT.setId(uuid);
		when(prim.get(uuid)).thenReturn(A_PROJECT);
		pServ.addEmail("{\"email\":\"a@a.a\"}", uuid);
		verify(prim).get(uuid);
	}

	@Test
	public void testAddEmail_shouldPass_whenEvtgOk() throws JsonParseException, JsonMappingException, IOException {
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		when(prim.get(uuid)).thenReturn(proj);
		pServ.addEmail("{\"email\":\"hlld@hotmail.fr\"}", uuid);
		verify(prim).get(uuid);
	}

	@Test
	public void testAddEmail_shouldntDoAnything_whenEmailAlreadyExist()
			throws JsonParseException, JsonMappingException, IOException {
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		List<String> list = new ArrayList<String>();
		list.add("hlld@hotmail.fr");
		proj.setMails(list);
		when(prim.get(uuid)).thenReturn(proj);
		pServ.addEmail("{\"email\":\"hlld@hotmail.fr\"}", uuid);
		verify(prim).get(uuid);
	}

	@Test
	public void testGetProjects_shouldNotBeNull_whenProjectIn() {
		List<Project> lp = new ArrayList<Project>();
		lp.add(new Project("test"));
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
		Project proj = new Project("test");
		proj.setId("202d4355-6a2e-4269-8ca9-49095acfe210");
		when(prim.get("202d4355-6a2e-4269-8ca9-49095acfe210")).thenReturn(proj);
		assertTrue(pServ.getProject("202d4355-6a2e-4269-8ca9-49095acfe210") != null);
		verify(prim).get("202d4355-6a2e-4269-8ca9-49095acfe210");
	}
	
	@Test
	public void testgetNode_shouldBeNull_whenWrongJson() {

		assertEquals(pServ.getNode("sfsf", "title"), null);

	}
	@Test
	public void testgetNode_shouldBeNull_whenTitleNull() {

		assertEquals(pServ.getNode("{\"author\" : \"Hugo\"", "title"), null);

	}

	@Test
	public void testValidJson_shouldBeFalse_whenWrongJson() {
		assertEquals(pServ.validJson("dfdsf"), false);
	}

	@Test
	public void testValidJson_shouldBeTrue_whenGoodJson() {
		assertEquals(pServ.validJson("{\"email\":\"hlld@hotmail.fr\"}"), true);
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testsendMail_shouldBeAnERDAEException_whenNoProps() {		
		when(ms.checkProperties()).thenReturn(false);
		pServ.sendMail("202d4355-6a2e-4269-8ca9-49095acfe210");
		verify(ms).checkProperties();
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testsendMail_shouldBeAnERDAEException_whenUUIDNull() {
		pServ.sendMail(null);
	}
	@Test(expected = EmptyResultDataAccessException.class)
	public void testsendMail_shouldBeAnERDAEException_whenProjectNull() {		
		
		when(prim.get("202d4355-6a2e-4269-8ca9-49095acfe210")).thenReturn(null);
		pServ.sendMail("202d4355-6a2e-4269-8ca9-49095acfe210");
		verify(prim).get("202d4355-6a2e-4269-8ca9-49095acfe210");
	}

	@Test
	public void testsendMail_shouldNotBeAnERDAEException_whenNothingNullAndPropsOk() {		
		String uuid ="202d4355-6a2e-4269-8ca9-49095acfe210";
		Project proj = new Project("test");
		proj.setId(uuid);
		proj.getMails().add("azerty@poiuy.com");
		proj.getMails().add("hellotest@mails.com");
		when(prim.get(uuid)).thenReturn(proj);
		when(ms.checkProperties()).thenReturn(true);
		pServ.sendMail("202d4355-6a2e-4269-8ca9-49095acfe210");		
		pServ.sendMail(uuid);
		verify(prim,atLeast(1)).get(uuid);
		verify(ms,atLeast(1)).checkProperties();
	}
}
