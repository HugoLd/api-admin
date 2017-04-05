package org.cap.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import java.util.Properties;

import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

@org.springframework.context.annotation.Configuration
@Service
@PropertySource("classpath:/mail.properties")
public class MailService {
	@Autowired
	private Environment environment; // getting environment for properties
	JavaMailSender mailSender;
	@Autowired
	Configuration freemarkerConfiguration;

	/**
	 * Initialize the mailsender with properties
	 * 
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 * @throws URISyntaxException
	 */

	public boolean sendEmail(Map<String, Object> templatedMimeMessage, String addressTo)
			throws IOException, TemplateException {
		Template temp;
		mailSender = initMailSender();
		FileTemplateLoader templateLoader = null;
		templateLoader = new FileTemplateLoader(
				new File(getClass().getClassLoader().getResource("template").getFile()));
		freemarkerConfiguration.setTemplateLoader(templateLoader);
		temp = freemarkerConfiguration.getTemplate("mailTemplate.ftl");
		mailSender.send(initPreparator(templatedMimeMessage, addressTo, temp));
		return true;
	}

	/**
	 * initialize the MimeMessagePreparator
	 * 
	 * @param templatedMimeMessage
	 * @param addresses
	 * @param temp
	 * @return MimeMessagePreparator
	 * @throws IOException
	 * @throws TemplateException
	 */
	public MimeMessagePreparator initPreparator(Map<String, Object> templatedMimeMessage, String addressTo,
			Template temp) throws IOException, TemplateException {
		return new MimeMessagePreparator() {
			String messageText = FreeMarkerTemplateUtils.processTemplateIntoString(temp, templatedMimeMessage);

			@Override
			public void prepare(MimeMessage message) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.addTo(addressTo);
				helper.setFrom(getAddress());
				helper.setSubject("Daily mood");
				helper.setText(messageText, true);
			}

		};
	}

	/**
	 * initialize the sender
	 * 
	 * @return
	 */
	public JavaMailSender initMailSender() {
		JavaMailSenderImpl jms = new JavaMailSenderImpl();
		jms.setHost(environment.getProperty("smtp.host"));
		jms.setPort(Integer.parseInt(environment.getProperty("smtp.port")));
		jms.setUsername(environment.getProperty("smtp.address"));
		jms.setPassword(environment.getProperty("smtp.password"));

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.debug", "true");
		jms.setJavaMailProperties(props);
		return jms;

	}

	/**
	 * 
	 * @return the from address
	 */
	public String getAddress() {
		return environment.getProperty("smtp.address");
	}

	/**
	 * 
	 * @return the today's date
	 */
	public String getDateNowWithDayOfWeek() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy");
		return formatter.format(date);
	}
	
	/**
	 * 
	 * @return the today's date
	 */
	public String getDateNow() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date);
	}

	/**
	 * generate unique links
	 * 
	 * @param p
	 * @param mail 
	 * @param date 
	 * @return
	 */
	public String[] generateLinks(String uuid, String mail, String date) {
		String baseLink = environment.getProperty("smtp.baseLink");
		String uuMood = uuid+"#"+mail+"#"+date;
		String[] tabDate = new String[5];
		tabDate[0] = baseLink +"?uuid="+ uuid+"&date="+ date+"&mood="+"0&uuidmood="+uuMood;
		tabDate[1] = baseLink +"?uuid="+ uuid+"&date="+ date+"&mood="+"1&uuidmood="+uuMood;
		tabDate[2] = baseLink +"?uuid="+ uuid+"&date="+ date+"&mood="+"2&uuidmood="+uuMood;
		tabDate[3] = baseLink +"?uuid="+ uuid+"&date="+ date+"&mood="+"3&uuidmood="+uuMood;
		tabDate[4] = baseLink +"?uuid="+ uuid+"&date="+ date+"&mood="+"4&uuidmood="+uuMood;
		return tabDate;
	}

	public boolean checkProperties() {
		if(environment.getProperty("smtp.baseLink") != null && environment.getProperty("smtp.host") != null && environment.getProperty("smtp.port") != null && environment.getProperty("smtp.address") != null &&environment.getProperty("smtp.password") != null)
			return true;
		return false;
	}

}
