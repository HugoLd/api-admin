package org.cap.repo;

import java.util.List;

import javax.annotation.PostConstruct;
import org.cap.bean.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
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
		checkProperties();
		MongoClient mongo = new MongoClient(environment.getProperty("mongo.host"),
				Integer.parseInt(environment.getProperty("mongo.port")));
		mongoTemplate = new MongoTemplate(mongo, environment.getProperty("mongo.database"));

	}
	/**
	 * check if properties are well set
	 * @return boolean
	 */
	public void checkProperties(){
		if(environment.getProperty("mongo.host") == null || environment.getProperty("mongo.port") == null || environment.getProperty("mongo.database") == null){
			throw new EmptyResultDataAccessException(0);
		}
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
		if(object == null){
			throw new EmptyResultDataAccessException(0);
		}
		mongoTemplate.insert(object, "projects");

	}

	/**
	 * return the first project to match the given title
	 * 
	 * @param title
	 * @return
	 */
	public Project getObjectByTitle(String title) {
		if(title == null){
			throw new EmptyResultDataAccessException(0);
		}
		return mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), Project.class, "projects");

	}

	/**
	 * return the first project to match the given UUID
	 * 
	 * @param title
	 * @return project
	 */
	public Project getObject(String id) {
		if(id == null){
			throw new EmptyResultDataAccessException(0);
		}
		return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), Project.class, "projects");

	}
	
	public void deleteObject(String uuid) {
		if(uuid == null){
			throw new EmptyResultDataAccessException(0);
		}
		mongoTemplate.remove(new Query(Criteria.where("_id").is(uuid)), Project.class,"projects");
	}
	/**
	 * get all the projects in the base ( format _id , title)
	 * @return
	 */
	public List<Project> getAllObjects() {	
		Query q = new Query();
		q.fields().include("_id");
		q.fields().include("title");		
		return mongoTemplate.find(q,Project.class, "projects");
	}
	
	

}
