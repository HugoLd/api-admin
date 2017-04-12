package org.cap.bean;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Bean Project annotated to be insert in a mongo db
 * 
 * @author hledall
 *
 */
@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Project {
	@Id
	private String id;
	private String title;
	private List<String> mails;

	public Project() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getMails() {
		if(mails == null){
			mails = new ArrayList<String>();
		}
		return mails;
	}

	public void setMails(List<String> mails) {
		this.mails = mails;
	}

}
