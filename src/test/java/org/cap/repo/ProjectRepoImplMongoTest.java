package org.cap.repo;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.cap.bean.Project;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class ProjectRepoImplMongoTest extends TestCase{
	@InjectMocks
	ProjectRepoImplMongo prim ;
	@Mock
	Environment environment;
	final String AN_UUID = "UnitTestUuid";
	final Project A_PROJECT = new Project();
	final Project A_PROJECT_2 = new Project();
	final Project A_PROJECT_3 = new Project();
	@Before
	public void setUp() throws Exception {
		
		when(environment.getProperty("mongo.host")).thenReturn("localhost");
		when(environment.getProperty("mongo.port")).thenReturn("27017");
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		prim.init();
		super.setUp();
		List<String> listMails = new ArrayList<String>();
		listMails.add("hello@mail.com");
		A_PROJECT.setId(AN_UUID);
		A_PROJECT.setTitle("Testing");
		A_PROJECT.setMails(listMails);
		prim.save(A_PROJECT);
		
		A_PROJECT_2.setId(AN_UUID+"delete");
		A_PROJECT_2.setTitle("Testingdelete");
		A_PROJECT_2.setMails(listMails);
		prim.save(A_PROJECT_2);
		
	}
	
	
	@Test
	public void testInit_ShouldNotThrowException_whenInitialized()
	{
		when(environment.getProperty("mongo.host")).thenReturn("localhost");
		when(environment.getProperty("mongo.port")).thenReturn("27017");
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		prim.init();
		verify(environment,atLeast(1)).getProperty("mongo.host");
		verify(environment,atLeast(1)).getProperty("mongo.port");
		verify(environment,atLeast(1)).getProperty("mongo.database");
		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInit_ShouldThrowException_whenNotInitialized()
	{
		when(environment.getProperty("mongo.host")).thenReturn(null);
		when(environment.getProperty("mongo.port")).thenReturn("27017");
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		prim.init();
		verify(environment,atLeast(1)).getProperty("mongo.host");
		verify(environment,atLeast(1)).getProperty("mongo.port");
		verify(environment,atLeast(1)).getProperty("mongo.database");
		
	}
	@Test
	public void testGetAll_ShouldNotBeEmpty_whenDBContains()
	{
		assertFalse(prim.getAll().get(0) == null);		
	}
	@Test
	public void testGetAllWithMail_ShouldNotBeEmpty_whenDBContains()
	{
		assertFalse(prim.getAllWithMails().get(0).getMails() == null);		
	}
	@Test
	public void testGet_ShouldNotBeEmpty_whenDBContains()
	{
		assertFalse(prim.get(A_PROJECT.getId()) == null);		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testGet_ShouldThrowException_whenDBContains()
	{
		prim.get(null);		
	}
	@Test
	public void testGetObjectByTitle_ShouldBeAProject_whenDBContains()
	{
		assertFalse(prim.getObjectByTitle(A_PROJECT.getTitle()).getId() == null);		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testGetObjectByTitle_ShouldThrowException_whenTitleNull()
	{
		prim.getObjectByTitle(null);		
	}
	@Test
	public void testSave_ShouldNotThrowException_whenSaved()
	{
		List<String> listMails = new ArrayList<String>();
		listMails.add("test@mail.com");
		Project p = new Project();
		p.setId(AN_UUID+"save");
		p.setTitle("Testing");
		p.setMails(listMails);
		prim.save(p);
	}
	@Test
	public void testDelete_ShouldNotThrowException_whenDeleted()
	{		
		prim.delete(AN_UUID+"delete");		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testDelete_ShouldThrowIAException_whenIdNull()
	{		
		prim.delete(null);		
	}
	@Test
	public void testDeleteUser_ShouldListChange_whenIdNull()
	{		
		int mailBef = prim.get(AN_UUID).getMails().size();
		prim.deleteUser("hello@mail.com",AN_UUID);		
		int mailAf = prim.get(AN_UUID).getMails().size();
		assertTrue(mailAf< mailBef);
	}
	@Test
	public void testUpdate_ShouldNotThrowException_whenUpdated()
	{
		Project p = prim.get(AN_UUID);
		p.setTitle(p.getTitle()+"2");
		prim.update(p);		
	}
	@After
	public void tearDown(){
		try{
			super.tearDown();
		}
		catch(Exception e){			
		}
		try{
			prim.delete(AN_UUID+"save");
		}
		catch(Exception e){			
		}
		try{
			prim.delete(AN_UUID+"delete");
		}
		catch(Exception e){			
		}
		try{
			prim.delete(AN_UUID);
		}
		catch(Exception e){			
		}
	}
	
}
