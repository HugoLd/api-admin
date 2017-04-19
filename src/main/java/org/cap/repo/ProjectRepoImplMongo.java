package org.cap.repo;

import java.util.List;

import javax.annotation.PostConstruct;
import org.cap.bean.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
	 * Empty Constructor
	 */
	public ProjectRepoImplMongo() {
	}

	/**
	 * init the Template after constructor getting infos in properties file
	 */
	@PostConstruct
	protected void init() {
		checkProperties();
		MongoClient mongo = new MongoClient(environment.getProperty("mongo.host"),
				Integer.parseInt(environment.getProperty("mongo.port")));
		mongoTemplate = new MongoTemplate(mongo, environment.getProperty("mongo.database"));

	}
	/**
	 * check if properties are well set
	 * @return boolean
	 */
	private void checkProperties(){
		if(environment.getProperty("mongo.host") == null || environment.getProperty("mongo.port") == null || environment.getProperty("mongo.database") == null){
			throw new IllegalArgumentException("At least one property missing");
		}
	}
	

	/**
	 * insert project in db
	 */
	@Override
	public void save(Project object) {
		mongoTemplate.insert(object);
	}

	public void update(Project project) {
		mongoTemplate.updateFirst(
				new Query(Criteria.where("_id").is(project.getId())),
				new Update()
					.set("title", project.getTitle())
					.set("mails",project.getMails()),
				Project.class
				);
	}

	/**
	 * return the first project to match the given title
	 * 
	 * @param title
	 * @return
	 */
	public Project getObjectByTitle(String title) {
		if(title == null) {
			throw new IllegalArgumentException("UUID null");
		}
		return mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), Project.class);
	}

	/**
	 * return the first project to match the given UUID
	 * 
	 * @param title
	 * @return project
	 */
	public Project get(String id) {
		if(id == null){
			throw new IllegalArgumentException("UUID null");
		}
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), Project.class);

	}
	/**
	 * delete a project
	 */
	public void delete(String uuid) {
		if(uuid == null){
			throw new IllegalArgumentException("UUID null");
		}
		mongoTemplate.remove(new Query(Criteria.where("_id").is(uuid)), Project.class);
	}
	/**
	 * get all the projects in the base ( format _id , title)
	 * @return
	 */
	public List<Project> getAll() {	
		Query q = new Query();
		q.fields().include("_id");
		q.fields().include("title");		
		return mongoTemplate.find(q,Project.class);
	}
	/**
	 * get all the projects with e mail addresses
	 * @return
	 */
	public List<Project> getAllWithMails() {	
		Query q = new Query();
		q.fields().include("_id");
		q.fields().include("title");	
		q.fields().include("mails");
		return mongoTemplate.find(q,Project.class);
	}
	/**
	 * update a project to delete an user
	 * @param email
	 * @param uuidProj
	 */
	public void deleteUser(String email, String uuidProj) {
		Project proj = get(uuidProj);
		proj.getMails().remove(email);
		update(proj);
	}
	
	

}
