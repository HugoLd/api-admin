package org.cap.repo;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.cap.bean.Mood;
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
public class MoodRepoImplMongoTest extends TestCase{
	@InjectMocks
	MoodRepoImplMongo mrim ;
	@Mock
	Environment environment;
	final String AN_UUID = "UnitTestUuid";
	final Mood A_MOOD = new Mood();
	final Mood A_MOOD_2 = new Mood();
	@Before
	public void setUp() throws Exception {
		
		when(environment.getProperty("mongo.host")).thenReturn("localhost");
		when(environment.getProperty("mongo.port")).thenReturn("27017");
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		mrim.init();
		super.setUp();
		
		A_MOOD.setUuid(AN_UUID);
		A_MOOD.setComment("Testing");
		A_MOOD.setDate("1-1-0");
		A_MOOD.setMood(-1);
		A_MOOD.setUuidProj("UnitTestUuidProj");
		mrim.save(A_MOOD);
		
		A_MOOD_2.setUuid(AN_UUID+"delete");
		A_MOOD_2.setComment("Testing");
		A_MOOD_2.setDate("1-1-0");
		A_MOOD_2.setMood(-1);
		A_MOOD_2.setUuidProj("UnitTestUuidProj");
		mrim.save(A_MOOD_2);
	}
	
	
	@Test
	public void testInit_ShouldNotThrowException_whenInitialized()
	{
		when(environment.getProperty("mongo.host")).thenReturn("localhost");
		when(environment.getProperty("mongo.port")).thenReturn("27017");
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		mrim.init();
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
		mrim.init();
		verify(environment,atLeast(1)).getProperty("mongo.host");
		verify(environment,atLeast(1)).getProperty("mongo.port");
		verify(environment,atLeast(1)).getProperty("mongo.database");
		
	}
	@Test
	public void testGetAll_ShouldNotBeEmpty_whenDBContains()
	{
		assertFalse(mrim.getAll().get(0) == null);		
	}
	@Test
	public void testGetProjectMoods_ShouldNotBeEmpty_whenDBContains()
	{
		assertFalse(mrim.getProjectMoods(mrim.getAll().get(0).getUuidProj()).get(0) == null);		
	}
	@Test
	public void testGet_ShouldReturnAMood_whenDBContains()
	{
		assertFalse(mrim.get(mrim.getAll().get(0).getUuid()) == null);		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testGet_ShouldThrowException_whenUuidNull()
	{
		mrim.get(null);		
	}
	@Test
	public void testSave_ShouldNotThrowException_whenSaved()
	{
		Mood m = new Mood();
		m.setUuid(AN_UUID+"save");
		m.setComment("Testing");
		m.setDate("1-1-0");
		m.setMood(-1);
		m.setUuidProj("UnitTestUuidProj");
		mrim.save(m);		
	}
	@Test
	public void testSave_ShouldNotThrowException_whenUpdated()
	{
		Mood m = mrim.get(AN_UUID);
		m.setComment(m.getComment()+"1");
		mrim.save(m);		
	}
	@Test
	public void testDelete_ShouldNotThrowException_whenDeleted()
	{		
		mrim.delete(AN_UUID+"delete");		
	}
	@Test(expected = IllegalArgumentException.class)
	public void testDelete_ShouldThrowIAException_whenUUIDNull()
	{		
		mrim.delete(null);		
	}
	@Test
	public void testUpdate_ShouldNotThrowException_whenUpdated()
	{
		Mood m = mrim.get(AN_UUID);
		m.setComment(m.getComment()+"2");
		mrim.update(m);		
	}
	@After
	public void tearDown(){
		try{
			super.tearDown();
		}
		catch(Exception e){			
		}
		try{
			mrim.delete(AN_UUID+"save");
		}
		catch(Exception e){			
		}
		try{
			mrim.delete(AN_UUID+"delete");
		}
		catch(Exception e){			
		}
		try{
			mrim.delete(AN_UUID);
		}
		catch(Exception e){			
		}
	}
	
}
