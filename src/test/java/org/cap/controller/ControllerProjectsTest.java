package org.cap.controller;

import org.cap.repo.ProjectRepoImplMongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
	public void everTrue() throws Exception{
		assertTrue(true);
	}
	
}
