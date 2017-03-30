package org.cap.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	protected String id;
	protected String title;
	protected ArrayList<String> mails;

	/**
	 * Constructor generating UUID
	 * 
	 * @param title
	 */
	public Project(String title) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		mails = null;
	}
	
	// <accessors>
	/**
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param Id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 
	 * @return
	 */
	public List<String> getMails() {
		if(mails == null){
			mails = new ArrayList<String>();
		}
		return mails;
	}
	/**
	 * 
	 * @param mails
	 */
	public void setMails(ArrayList<String> mails) {
		this.mails = mails;
	}

	// </accessors>

	

}
