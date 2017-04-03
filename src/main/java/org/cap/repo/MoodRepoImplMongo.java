package org.cap.repo;

import java.util.List;

import javax.annotation.PostConstruct;

import org.cap.bean.Mood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
@PropertySource("classpath:/mongoConfig.properties")
public class MoodRepoImplMongo implements Repo<Mood>{
	private MongoTemplate mongoTemplate;
	@Autowired
	private Environment environment; // getting environment for properties


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

	@Override
	public List<Mood> getAllObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveObject(Mood object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mood getObject(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteObject(String id) {
		// TODO Auto-generated method stub
		
	}

}
