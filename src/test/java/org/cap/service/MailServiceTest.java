package org.cap.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.cap.repo.ProjectRepoImplMongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import junit.framework.TestCase;

public class MailServiceTest extends TestCase {

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
	/*
	@Test(expected = EmptyResultDataAccessException.class)
	public void testAddEmail_shouldBeAnERDAEException_whenProjectDoesntExist()
			throws JsonParseException, JsonMappingException, IOException {
		String uuid = "202d4355-6a2e-4269-8ca9-49095acfe210";
		when(prim.getObject(uuid)).thenThrow(new EmptyResultDataAccessException(0));
		pServ.addEmail("{\"email\":\"hello@gmail.com\"}", uuid);
		verify(prim).getObject(uuid);
	}
	*/
}
