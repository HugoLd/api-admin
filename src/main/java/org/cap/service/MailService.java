package org.cap.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.cap.bean.MoodValue;
import org.cap.utils.Util;
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
	Environment environment; // getting environment for properties
	JavaMailSender mailSender;
	@Autowired
	Configuration freemarkerConfiguration;
	private final String MAIL_TEMPLATE = "mailTemplate.ftl";

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
		if(getClass().getClassLoader().getResource("template") != null){
			templateLoader = new FileTemplateLoader(new File(getClass().getClassLoader().getResource("template").getFile()));			
			freemarkerConfiguration.setTemplateLoader(templateLoader);
			temp = freemarkerConfiguration.getTemplate(MAIL_TEMPLATE);
			mailSender.send(initPreparator(templatedMimeMessage, addressTo, temp));
			return true;
		}
		return false;
		
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
	private MimeMessagePreparator initPreparator(Map<String, Object> templatedMimeMessage, String addressTo,
			Template temp) throws IOException, TemplateException {
		return new MimeMessagePreparator() {
			String messageText = FreeMarkerTemplateUtils.processTemplateIntoString(temp, templatedMimeMessage);

			@Override
			public void prepare(MimeMessage message) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.addTo(addressTo);
				helper.setFrom(getAddress());
				helper.setSubject(environment.getProperty("mail.subject"));
				helper.setText(messageText, true);
			}

		};
	}

	/**
	 * initialize the sender
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	protected JavaMailSender initMailSender() {
		JavaMailSenderImpl jms = new JavaMailSenderImpl();
		jms.setHost(environment.getProperty("smtp.host"));
		jms.setPort(Integer.parseInt(environment.getProperty("smtp.port")));		
		if (Boolean.parseBoolean(environment.getProperty("smtp.auth"))) {
			
			jms.setUsername(environment.getProperty("smtp.address"));
			jms.setPassword(environment.getProperty("smtp.password"));
		}
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "\""+environment.getProperty("smtp.auth")+"\"");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.debug", "true");
		jms.setJavaMailProperties(props);
		return jms;

	}

	/**
	 * get the addreww from properties
	 * @return the from address
	 */
	private String getAddress() {
		return environment.getProperty("smtp.address");
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
		String uuMood = Util.generateUUID(uuid, mail, date);
		String[] tabDate = new String[5];
		tabDate[4] = baseLink + "?uuidProj=" + uuid + "&uuid=" + uuMood + "&date=" + date + "&mood=" + MoodValue.REALLYGOOD.getValue();
		tabDate[3] = baseLink + "?uuidProj=" + uuid + "&uuid=" + uuMood + "&date=" + date + "&mood=" + MoodValue.GOOD.getValue();
		tabDate[2] = baseLink + "?uuidProj=" + uuid + "&uuid=" + uuMood + "&date=" + date + "&mood=" + MoodValue.AVERAGE.getValue();
		tabDate[1] = baseLink + "?uuidProj=" + uuid + "&uuid=" + uuMood + "&date=" + date + "&mood=" + MoodValue.BAD.getValue();
		tabDate[0] = baseLink + "?uuidProj=" + uuid + "&uuid=" + uuMood + "&date=" + date + "&mood=" + MoodValue.REALLYBAD.getValue();
		return tabDate;
	}
	/**
	 * check if properties are ok else send illegalArgument
	 * @return
	 */
	public boolean checkProperties() {
		if (environment.getProperty("smtp.baseLink") != null && environment.getProperty("smtp.host") != null
				&& environment.getProperty("smtp.port") != null && environment.getProperty("smtp.address") != null
				&& environment.getProperty("smtp.password") != null && environment.getProperty("smtp.auth") != null)
			return true;
		throw new IllegalArgumentException("At least one property missing");
	}

}
