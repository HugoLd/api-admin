package org.cap.bean;


import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Project implements ObjectDB {
	@Id
	private UUID _id; 
	private String title;
	
    
    @SuppressWarnings("static-access")
	public Project(String title){
    	this._id = _id.randomUUID();
    	this.title = title;
    }


	public UUID get_id() {
		return _id;
	}


	public void set_id(UUID _id) {
		this._id = _id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toJson(){
		
		return "{\"_id\" : \""+this._id+"\" ,\"title\" : \""+this.title+"\"}" ;
		
	}
    
	
}
