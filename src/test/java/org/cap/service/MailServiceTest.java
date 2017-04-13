package org.cap.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
@Configuration
public class MailServiceTest extends TestCase {
	@InjectMocks
	MailService mailService;
	@Mock
	Environment env;
	@Mock
	freemarker.template.Configuration freemarkerConfiguration;
	public static final String AN_EMAIL = "hlld@hotmail.fr";

	@Before
	public void setUp() throws Exception {
		
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testGetDateNowWithDayOfWeek_shouldContainsSlash_whenCall(){
		assertEquals(mailService.getDateNowWithDayOfWeek().contains("/"),true);
	}

	@Test
	public void testGetDateNow_shouldContainsMinus_whenCall(){
		System.out.println(mailService.getDateNow());
		assertEquals(mailService.getDateNow().contains("-"),true);
	}
	@Test
	public void testGenerateLinks_souldContainsStrings_WhenParamsOk(){
		when(env.getProperty("smtp.baseLink")).thenReturn("google.fr");
		assertTrue(mailService.generateLinks("oifsqfjldslkjf-dsfsdlms-dsfls", AN_EMAIL, "21-03-2017")[3] != null);
		verify(env).getProperty("smtp.baseLink");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testCheckProperties_souldBeIAException_WhenPropNull(){
		when(env.getProperty("smtp.baseLink")).thenReturn(null);
		mailService.checkProperties();
		verify(env).getProperty("smtp.baseLink");
	}
	@Test
	public void testCheckProperties_souldBeTrue_WhenParamsOk(){
		when(env.getProperty("smtp.baseLink")).thenReturn("google.fr");
		when(env.getProperty("smtp.host")).thenReturn("host");
		when(env.getProperty("smtp.port")).thenReturn("587");
		when(env.getProperty("smtp.address")).thenReturn("hello@mail.fr");
		when(env.getProperty("smtp.password")).thenReturn("123");		
		when(env.getProperty("smtp.auth")).thenReturn("true");	
		assertTrue(mailService.checkProperties());
		verify(env).getProperty("smtp.auth");
		verify(env).getProperty("smtp.baseLink");
		verify(env).getProperty("smtp.host");
		verify(env).getProperty("smtp.port");
		verify(env).getProperty("smtp.address");
		verify(env).getProperty("smtp.password");
	}
	@Test
	public void testGetAddress_souldContainsAt_WhenCalled(){
		when(env.getProperty("smtp.address")).thenReturn("hello@mail.fr");
		assertTrue(mailService.getAddress().contains("@"));
		verify(env).getProperty("smtp.address");
	}
	@Test
	public void testInitMailSender_souldCallPwdAndAdr_WhenAuthTrue(){
		when(env.getProperty("smtp.host")).thenReturn("host");
		when(env.getProperty("smtp.port")).thenReturn("587");
		when(env.getProperty("smtp.auth")).thenReturn("true");
		when(env.getProperty("smtp.address")).thenReturn("hello@mail.fr");
		when(env.getProperty("smtp.password")).thenReturn("123");		
		assertTrue(mailService.initMailSender() != null);
		verify(env).getProperty("smtp.host");
		verify(env).getProperty("smtp.port");
		verify(env,times(2)).getProperty("smtp.auth");
		verify(env).getProperty("smtp.address");
		verify(env).getProperty("smtp.password");
	}
	@Test
	public void testInitMailSender_souldNotBeNull_WhenCalled(){
		when(env.getProperty("smtp.host")).thenReturn("host");
		when(env.getProperty("smtp.port")).thenReturn("587");
		when(env.getProperty("smtp.auth")).thenReturn("false");
		when(env.getProperty("smtp.address")).thenReturn("hello@mail.fr");
		when(env.getProperty("smtp.password")).thenReturn("123");		
		assertTrue(mailService.initMailSender() != null);
		verify(env).getProperty("smtp.host");
		verify(env).getProperty("smtp.port");
		verify(env,times(2)).getProperty("smtp.auth");
		verify(env,never()).getProperty("smtp.address");
		verify(env,never()).getProperty("smtp.password");
	}
	
	
	
	
	

}
