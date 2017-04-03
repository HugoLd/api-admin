package org.cap.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Mood {

	private String uuid;	
	private int mood; 
	private String comment;
	private String date;
	
	public Mood(String uuid,int mood, String comment,String date){
		this.uuid = uuid;
		this.mood = mood;
		this.comment = comment;
		this.date = date;		
	}
	public Mood(){
		
	}
	
	
}
