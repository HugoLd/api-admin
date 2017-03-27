package org.cap.repo;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.cap.bean.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.MongoClient;

/**
 * Repo implementation for Project
 * 
 * @author hledall
 *
 */
@Configuration
@PropertySource("classpath:/mongoConfig.properties")
public class ProjectRepoImplMongo implements Repo<Project> {

	private MongoTemplate mongoTemplate;
	@Autowired
	private Environment environment; // getting environment for properties

	/**
	 * init the Template after constructor getting infos in properties file
	 */
	@PostConstruct
	public void init() {
		MongoClient mongo = new MongoClient(environment.getProperty("host"),
				Integer.parseInt(environment.getProperty("port")));
		mongoTemplate = new MongoTemplate(mongo, environment.getProperty("database"));

	}

	/**
	 * Empty Constructor
	 */
	public ProjectRepoImplMongo() {
	}

	/**
	 * insert project in db
	 */
	@Override
	public void saveObject(Project object) {
		mongoTemplate.insert(object, "projects");

	}
	/**
	 * return the first project to match the given title
	 * @param title
	 * @return
	 */
	public Project getObjectByTitle(String title) {

		return mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), Project.class, "projects");
		
	}
	
	/**
	 * return the first project to match the given UUID
	 * @param title
	 * @return
	 */
	public Project getObjectByID(UUID id) {

		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), Project.class, "projects");
		
	}

	/*
	 * NEXT SPRINT
	 * 
	 * @Override public List<Project> getAllObjects() { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 * @Override public WriteResult updateObject(String id, String name) { //
	 * TODO Auto-generated method stub return null; }
	 * 
	 * @Override public void deleteObject(String id) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 */

}
