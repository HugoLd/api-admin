package org.cap.bean;

import java.util.ArrayList;
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
	protected String _id;
	protected String title;
	protected ArrayList<String> listMail;

	/**
	 * Constructor generating UUID
	 * 
	 * @param title
	 */
	public Project(String title) {
		this._id = UUID.randomUUID().toString();
		this.title = title;
		listMail = null;
	}
	
	public void addToList(String s){
		checkListNullOrEmpty();
		listMail.add(s);
	}
	public void checkListNullOrEmpty(){
		if( listMail == null){
			listMail = new ArrayList<String>();
		}
	}
	// <accessors>
	/**
	 * 
	 * @return the id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * 
	 * @param Id
	 */
	public void set_id(String _id) {
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
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getListMail() {
		return listMail;
	}
	/**
	 * 
	 * @param listMail
	 */
	public void setListMail(ArrayList<String> listMail) {
		this.listMail = listMail;
	}

	// </accessors>

	

}
