package org.cap.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cap.bean.Mood;
import org.cap.bean.Project;
import org.cap.repo.MoodRepoImplMongo;
import org.cap.repo.ProjectRepoImplMongo;
import org.cap.utils.Util;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MoodServiceTest {
	@InjectMocks
	MoodService moodServ;
	@Mock
	MoodRepoImplMongo mrim;
	@Mock
	ProjectRepoImplMongo prim;
	@Mock
	public Util util;
	public static final List<List<Mood>> A_LIST_OF_LIST = new ArrayList<List<Mood>>();
	public static final List<Mood> A_LIST = new ArrayList<Mood>();
	public static final Project A_PROJECT = new Project();
	public static final String A_DATE = "23-01-2017";
	public static final String AN_UUID = UUID.nameUUIDFromBytes(("12345-6789-AZUSI"+"+hello@gmail.com+"+A_DATE).getBytes()).toString();
	public static final Mood A_MOOD = new Mood(AN_UUID,"12345-6789-AZUSI",0,"",A_DATE);

	@Before
	public void setUp() throws Exception {
		
		A_MOOD.setComment("commenttest");
		A_MOOD.setMood(2);
		A_LIST.add(A_MOOD);
		A_LIST_OF_LIST.add(A_LIST);
		A_PROJECT.setTitle("test");
		A_PROJECT.setId("12345-6789-AZUSI");
		A_PROJECT.getMails().add("hello@gmail.com");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetProjectMoods_shouldBeIAException_whenNull() {
		moodServ.getProjectMoods(null);
	}
	@Test
	public void testGetProjectMoods_shouldBeAListOfList_whenNotNull() {
		when(mrim.getProjectMoods(AN_UUID)).thenReturn(A_LIST);		
		assertEquals(moodServ.getProjectMoods(AN_UUID).getClass(), ArrayList.class);
		assertEquals(moodServ.getProjectMoods(AN_UUID).get(0).getClass(), ArrayList.class);		
		verify(mrim,times(2)).getProjectMoods(AN_UUID);
	}
	@Test
	public void testSaveMood_shouldNotBeException_whenMoodOk() {
		A_MOOD.setDate(Util.getDateNow());
		A_MOOD.setUuid(Util.generateUUID(A_PROJECT.getId(), "hello@gmail.com", Util.getDateNow()));		
		when(prim.get(A_PROJECT.getId())).thenReturn(A_PROJECT);		
		moodServ.saveMood(A_MOOD);
		verify(prim).get(A_PROJECT.getId());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSaveMood_shouldBeIAException_WhenMailNotIn(){
		Mood m = new Mood();
		m.setUuid("2738749");
		m.setDate(A_DATE);
		m.setUuidProj("12345-6789-AZUSI");
		when(prim.get("12345-6789-AZUSI")).thenReturn(A_PROJECT);		
		moodServ.saveMood(m);
		verify(prim).get("12345-6789-AZUSI");
	}
	@Test
	public void testPurgeProj_shouldNotBeException_whenEvtgOk() {
		List<Mood> list = new ArrayList<Mood>();
		list.add(new Mood("12345-6789-AZUSI",A_PROJECT.getId(),0," e","1-2"));
		list.add(new Mood("1345-6789-AZUSI",A_PROJECT.getId(),1,"e ","25-12-2018"));
		list.add(new Mood("12345-689-AZUSI",A_PROJECT.getId(),2,"f ","01-01-1001"));
		list.add(new Mood("12345-6789-ZUSI",A_PROJECT.getId(),3," f",Util.getDateNow()));
		
		A_MOOD.setDate(Util.getDateNow());
		A_MOOD.setUuid(Util.generateUUID(A_PROJECT.getId(), "hello@gmail.com", Util.getDateNow()));		
		when(mrim.getProjectMoods(A_PROJECT.getId())).thenReturn(list);		
		moodServ.purgeProj(A_PROJECT.getId());
		verify(mrim).getProjectMoods(A_PROJECT.getId());
	}


}
