package org.cap.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mood {
	@Id
	private String uuid;
	private String uuidProj;	
	private int mood; 
	private String comment;
	private String date;

	public Mood() {}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuidProj() {
		return uuidProj;
	}
	public void setUuidProj(String uuidProj) {
		this.uuidProj = uuidProj;
	}

	public int getMood() {
		return mood;
	}
	public void setMood(int mood) {
		this.mood = mood;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}	
	
}
