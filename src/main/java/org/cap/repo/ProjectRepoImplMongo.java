package org.cap.repo;

import javax.annotation.PostConstruct;

import org.cap.bean.Project;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.MongoClient;

@Configuration
@PropertySource("classpath:/mongoConfig.properties")
public class ProjectRepoImplMongo implements Repo<Project> {

	private MongoTemplate mongoTemplate;
	@Autowired
	private Environment env;
	private static Environment environment;

	@PostConstruct
	public void init() {
		environment = env;
		System.out.println(environment == env);

		MongoClient mongo = new MongoClient(environment.getProperty("host"),
				Integer.parseInt(environment.getProperty("port")));
		mongoTemplate = new MongoTemplate(mongo, environment.getProperty("database"));

	}

	public ProjectRepoImplMongo() {
	}

	@Override
	public void saveObject(Project object) {
		mongoTemplate.insert(object, "projects");

	}

	public Project getObjectByTitle(String title) {
		 
		return  mongoTemplate.findOne(new Query(Criteria.where("title").is(title)), Project.class,"projects");
		//return mongoTemplate.findOne(new BasicQuery("{ \"title\" : \"" + title + "\"}"), Project.class);
	}
	/*
	 * @Override public List<Project> getAllObjects() { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 * @Override public Project getObject(String id) { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 * 
	 * 
	 * @Override public WriteResult updateObject(String id, String name) { //
	 * TODO Auto-generated method stub return null; }
	 * 
	 * @Override public void deleteObject(String id) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * @Override public void createCollection() { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 * 
	 * @Override public void dropCollection() { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 */

}
