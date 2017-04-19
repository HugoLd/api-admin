package org.cap.service;

import static org.mockito.Mockito.atLeast;
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
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
	public void testInitMailSender_shouldUsrBeSet_WhenAuthTrue(){
		when(env.getProperty("smtp.host")).thenReturn("host");
		when(env.getProperty("smtp.port")).thenReturn("587");
		when(env.getProperty("smtp.address")).thenReturn("hello@mail.fr");
		when(env.getProperty("smtp.password")).thenReturn("123");		
		when(env.getProperty("smtp.auth")).thenReturn("true");	
		JavaMailSenderImpl jms = (JavaMailSenderImpl) mailService.initMailSender();
		assertFalse(jms.getUsername() == null);
		verify(env, atLeast(1)).getProperty("smtp.host");
		verify(env, atLeast(1)).getProperty("smtp.port");
		verify(env, atLeast(1)).getProperty("smtp.auth");
		verify(env, atLeast(1)).getProperty("smtp.address");
		verify(env, atLeast(1)).getProperty("smtp.password");
	}
	@Test
	public void testInitMailSender_shouldUsrNotBeSet_WhenAuthFalse(){
		when(env.getProperty("smtp.host")).thenReturn("host");
		when(env.getProperty("smtp.port")).thenReturn("587");
		when(env.getProperty("smtp.address")).thenReturn("hello@mail.fr");
		when(env.getProperty("smtp.password")).thenReturn("123");		
		when(env.getProperty("smtp.auth")).thenReturn("false");	
		JavaMailSenderImpl jms = (JavaMailSenderImpl) mailService.initMailSender();
		assertTrue(jms.getUsername() == null);
		verify(env, atLeast(1)).getProperty("smtp.host");
		verify(env, atLeast(1)).getProperty("smtp.port");
		verify(env, atLeast(1)).getProperty("smtp.auth");
	}
	
}
