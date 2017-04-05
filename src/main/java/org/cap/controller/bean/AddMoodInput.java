package org.cap.controller.bean;


public class AddMoodInput {
	private String uuid;
	private String uuidProj;
	private int mood; 
	private String comment;
	private String date;
	

	public AddMoodInput(String uuid,int mood,String comment,String date) {
		this.uuid = uuid;
		this.mood = mood;
		this.comment = comment;
		this.date = date;
	}

	public AddMoodInput() {
	}

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
