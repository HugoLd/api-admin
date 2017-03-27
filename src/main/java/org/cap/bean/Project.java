package org.cap.bean;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Bean Project annotated to be insert in a mongo db
 * 
 * @author hledall
 *
 */
@Document
public class Project implements ObjectDB {
	@Id
	private UUID _id;
	private String title;
	private ArrayList<String> listMail;

	/**
	 * Constructor generating UUID
	 * 
	 * @param title
	 */
	@SuppressWarnings("static-access")
	public Project(String title) {
		this._id = _id.randomUUID();
		this.title = title;
	}

	// <accessors>
	/**
	 * 
	 * @return the id
	 */
	public UUID get_id() {
		return _id;
	}

	/**
	 * 
	 * @param _id
	 */
	public void set_id(UUID _id) {
		this._id = _id;
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

	// </accessors>

	/**
	 * return the Project on Json format
	 */
	public String toJson() {

		return "{\"_id\" : \"" + this._id + "\" ,\"title\" : \"" + this.title + "\"}";

	}
	/**
	 * return the Project on Json format with the email list
	 */
	public String toJsonWithMembers() {
		if (listMail != null) {
			String json = "{\"_id\" : \"" + this._id + "\" ,\"title\" : \"" + this.title
					+ "\" \"members\" : [{\"mail\" : \"" + listMail.get(0)+"\"}";
			for(int i = 1;i<listMail.size();i++){
				json+= ",{\"mail\" : "+listMail.get(i)+"\"}";
			}
			json+="]";

			return json;
		}
		return null;

	}

}
