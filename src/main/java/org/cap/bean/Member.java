package org.cap.bean;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Member implements ObjectDB {

	private String email;

	@Override
	public String toJson() {
		
		return "{\"e-mail\" : \""+email+"\"}";
	}
	
}
