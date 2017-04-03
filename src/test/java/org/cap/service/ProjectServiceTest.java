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

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testIsAnEmail_shouldBeTrue_whenEmailIsOK() {
		assertEquals(pServ.IsAnEmail("hello@gmail.com"), true);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooShort() {
		assertEquals(pServ.IsAnEmail("h@m.f"), false);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailTooLong() {
		assertEquals(pServ.IsAnEmail("hellohellohellohellohello@emailadressprovier.unitedstates"), false);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailHasTwoAt() {
		assertEquals(pServ.IsAnEmail("hello@gm@ail.com"), false);
	}

	@Test
	public void testIsAnEmail_shouldBeFalse_whenEmailContainsNoPoint() {
		assertEquals(pServ.IsAnEmail("hello@gmailcom"), false);
	}

	@Test
	public void testCheckTitleNotExisting_shouldBeFalse_whenAlreadyExist() {
		when(prim.getObjectByTitle("Gestion")).thenReturn(new Project("Gestion"));
		assertEquals(pServ.checkTitleNotExisting("Gestion"), false);
		verify(prim).getObjectByTitle("Gestion");
	}

	@Test
	public void testCheckTitleNotExisting_shouldBeTrue_whenEmailNotExisting() {
		when(prim.getObjectByTitle("I'm not in")).thenReturn(null);
		assertEquals(pServ.checkTitleNotExisting("I'm not in"), true);
		verify(prim).getObjectByTitle("I'm not in");
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenJsonNull()
			throws JsonParseException, JsonMappingException, IOException {

		pServ.saveProjects(null);

	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenTitleNull()
			throws JsonParseException, JsonMappingException, IOException {

		pServ.saveProjects("{\"title\":null}");

	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testSaveProject_shouldBeAnERDAEException_whenTitleAlreadyExist()
			throws JsonParseException, JsonMappingException, IOException {
		when(prim.getObjectByTitle("hello")).thenReturn(new Project("hello"));
		pServ.saveProjects("{\"title\":\"hello\"}");
		verify(prim).getObjectByTitle("hello");

	}

	@Test
	public void testSaveProject_shouldBeAnERDAEException_whenTitleNotExist()
			throws JsonParseException, JsonMappingException, IOException {
		when(prim.getObjectByTitle("hello")).thenReturn(null);
		pServ.saveProjects("{\"title\":\"hello\"}");
		verify(prim).getObjectByTitle("hello");

	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenProjectDoesntExist()
			throws JsonParseException, JsonMappingException, IOException {
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		when(prim.getObject(uuid)).thenReturn(null);
		pServ.addEmail("{\"email\":\"hello@gmail.com\"}", uuid);

		verify(prim).getObject(uuid);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenEmailIsNull()
			throws JsonParseException, JsonMappingException, IOException {
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		when(prim.getObject(uuid)).thenReturn(proj);
		pServ.addEmail(null, uuid);

		verify(prim).getObject(uuid);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenEmailIncorrect()
			throws JsonParseException, JsonMappingException, IOException {
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		when(prim.getObject(uuid)).thenReturn(proj);
		pServ.addEmail("{\"email\":\"a@a.a\"}", uuid);
		verify(prim).getObject(uuid);
	}

	@Test
	public void testAddEmail_shouldPass_whenEvtgOk() throws JsonParseException, JsonMappingException, IOException {
		Project proj = new Project("hello");
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		proj.setId(uuid);
		when(prim.getObject(uuid)).thenReturn(proj);
		pServ.addEmail("{\"email\":\"hlld@hotmail.fr\"}", uuid);
		verify(prim).getObject(uuid);
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
		when(prim.getObject(uuid)).thenReturn(proj);
		pServ.addEmail("{\"email\":\"hlld@hotmail.fr\"}", uuid);
		verify(prim).getObject(uuid);
	}

	@Test
	public void testGetProjects_shouldNotBeNull_whenProjectIn() {
		List<Project> lp = new ArrayList<Project>();
		lp.add(new Project("test"));
		when(prim.getAllObjects()).thenReturn(lp);
		assertTrue(pServ.getProjects() != null);
		verify(prim).getAllObjects();
	}

	@Test
	public void testGetProjects_shouldBeNull_whenNoProjectIn() {
		when(prim.getAllObjects()).thenReturn(null);
		assertTrue(pServ.getProjects() == null);
		verify(prim).getAllObjects();
	}

	@Test
	public void testGetProject_shouldNotBeNull_whenProjectExist() {
		Project proj = new Project("test");
		proj.setId("202d4355-6a2e-4269-8ca9-49095acfe210");
		when(prim.getObject("202d4355-6a2e-4269-8ca9-49095acfe210")).thenReturn(proj);
		assertTrue(pServ.getProject("202d4355-6a2e-4269-8ca9-49095acfe210") != null);
		verify(prim).getObject("202d4355-6a2e-4269-8ca9-49095acfe210");
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
		
		when(prim.getObject("202d4355-6a2e-4269-8ca9-49095acfe210")).thenReturn(null);
		pServ.sendMail("202d4355-6a2e-4269-8ca9-49095acfe210");
		verify(prim).getObject("202d4355-6a2e-4269-8ca9-49095acfe210");
	}

	@Test
	public void testsendMail_shouldNotBeAnERDAEException_whenNothingNullAndPropsOk() {		
		String uuid ="202d4355-6a2e-4269-8ca9-49095acfe210";
		Project proj = new Project("test");
		proj.setId(uuid);
		proj.getMails().add("azerty@poiuy.com");
		proj.getMails().add("hellotest@mails.com");
		when(prim.getObject(uuid)).thenReturn(proj);
		when(ms.checkProperties()).thenReturn(true);
		pServ.sendMail("202d4355-6a2e-4269-8ca9-49095acfe210");		
		pServ.sendMail(uuid);
		verify(prim,atLeast(1)).getObject(uuid);
		verify(ms,atLeast(1)).checkProperties();
	}
}
