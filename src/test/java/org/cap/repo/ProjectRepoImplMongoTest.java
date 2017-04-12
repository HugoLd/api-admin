package org.cap.repo;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
@Configuration
public class ProjectRepoImplMongoTest extends TestCase{

	
	@InjectMocks
	ProjectRepoImplMongo pRepo;
	@Mock
	Environment environment;
	@Mock
	MongoTemplate mongoTemplate;
	
	@Before
	public void setUp() throws Exception {
		when(environment.getProperty("mongo.host")).thenReturn("localhost");
		when(environment.getProperty("mongo.port")).thenReturn("27017");
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		pRepo.init();
		verify(environment,atLeast(1)).getProperty("mongo.host");
		verify(environment,atLeast(1)).getProperty("mongo.port");
		verify(environment,atLeast(1)).getProperty("mongo.database");
		super.setUp();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCheckProperties_shouldBeIAException_whenParamNull() {
		when(environment.getProperty("mongo.host")).thenReturn("localhost");
		when(environment.getProperty("mongo.port")).thenReturn(null);
		when(environment.getProperty("mongo.database")).thenReturn("teamdb");
		pRepo.checkProperties();
		verify(environment,atLeast(1)).getProperty("mongo.host");
		verify(environment,atLeast(1)).getProperty("mongo.port");
		verify(environment,atLeast(1)).getProperty("mongo.database");
	}
	

	@Test
	public void testGetAll_shouldNotBeEmpty_whenCalled() {
		assertTrue(pRepo.getAll().get(0) != null);
	}

}
