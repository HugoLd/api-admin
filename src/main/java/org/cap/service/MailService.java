package org.cap.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeMessage;

import org.cap.bean.MailConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

@Service
public class MailService {
	JavaMailSender mailSender;
	@Autowired
	Configuration freemarkerConfiguration;
	@Autowired
	MailConfigurer config;

	/**
	 * Initialize the mailsender with properties
	 * 
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */

	public void sendEmail( Map<String, Object> templatedMimeMessage, List<String> addresses)
			throws IOException, TemplateException {
		String address = config.getAddress();
		Template temp;
		mailSender = config.initMailSender();
		FileTemplateLoader templateLoader = null;
		System.out.println(new File(getClass().getClassLoader().getResource("template").getFile()));
		templateLoader = new FileTemplateLoader(new File(getClass().getClassLoader().getResource("template").getFile()));
		freemarkerConfiguration.setTemplateLoader(templateLoader);
		temp = freemarkerConfiguration.getTemplate("mailTemplate.ftl");		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			String messageText = FreeMarkerTemplateUtils.processTemplateIntoString(
					temp, templatedMimeMessage);

			@Override
			public void prepare(MimeMessage message) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
				for (String address : addresses) {
					helper.addTo(address);
				}

				helper.setFrom(address);
				helper.setSubject("Daily mood");

				helper.setText(messageText,true);
			}

		};
		mailSender.send(preparator);
		System.out.println("sent");
	}

}
