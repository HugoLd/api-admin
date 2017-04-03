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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.MongoClient;

@Configuration
@PropertySource("classpath:/mongoConfig.properties")
public class MoodRepoImplMongo implements Repo<Mood> {
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
	 * 
	 * @return boolean
	 */
	public void checkProperties() {
		if (environment.getProperty("mongo.host") == null || environment.getProperty("mongo.port") == null
				|| environment.getProperty("mongo.database") == null) {
			throw new EmptyResultDataAccessException(0);
		}
	}
	/**
	 * get all the moods
	 */
	@Override
	public List<Mood> getAllObjects() {
		Query q = new Query();		
		return mongoTemplate.find(q,Mood.class, "moods");
	}
	/**
	 * get all the moods of a project
	 * @param uuid
	 * @return list mood
	 */
	public List<Mood> getProjectMoods(String uuid){
		Query q = new Query(Criteria.where("uuidProj").is(uuid));
		q.fields().include("mood");
		q.fields().include("date");		
		q.fields().include("comment");
		return mongoTemplate.find(q,Mood.class, "moods");
	}
	/**
	 * save a mood
	 */
	@Override
	public void saveObject(Mood object) {
		if (object == null) {
			throw new EmptyResultDataAccessException(0);
		}
		mongoTemplate.insert(object, "moods");
	}

	@Override
	public Mood getObject(String id) {
		if (id == null) {
			throw new EmptyResultDataAccessException(0);
		}
		return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(id)), Mood.class, "mood");

	}

	@Override
	public void deleteObject(String id) {

		if (id == null) {
			throw new EmptyResultDataAccessException(0);
		}
		mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), Mood.class, "moods");

	}

}
