package org.cap.bean;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
@PropertySource("classpath:/mail.properties")
public class MailConfigurer {

	@Autowired
	private Environment environment; // getting environment for properties
	/**
	 * initialize the sender
	 * @return
	 */
	public JavaMailSender initMailSender(){
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
    public String getAddress(){
    	return environment.getProperty("smtp.address");
    }
	
}
