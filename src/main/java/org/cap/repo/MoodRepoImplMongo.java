package org.cap.repo;

import java.util.List;

import javax.annotation.PostConstruct;

import org.cap.bean.Mood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
			throw new IllegalArgumentException("A least one property missing");
		}
	}

	/**
	 * get all the moods
	 */
	@Override
	public List<Mood> getAll() {
		Query q = new Query();
		return mongoTemplate.find(q, Mood.class, "moods");
	}

	/**
	 * get all the moods of a project
	 * 
	 * @param uuid
	 * @return list mood
	 */
	public List<Mood> getProjectMoods(String uuid) {
		Query q = new Query(Criteria.where("uuidProj").is(uuid));
		q.fields().include("mood");
		q.fields().include("date");
		q.fields().include("comment");
		return mongoTemplate.find(q, Mood.class, "moods");
	}

	/**
	 * save a mood
	 */
	@Override
	public void save(Mood object) {
		if (get(object.getUuid()) != null) {
			update(object);
		} else {
			mongoTemplate.insert(object);
		}

	}
	/**
	 * get a mood by id
	 */
	@Override
	public Mood get(String id) {
		if (id == null) {
			throw new IllegalArgumentException("UUID null");
		}
		return mongoTemplate.findOne(new Query(Criteria.where("uuid").is(id)), Mood.class);

	}
	/**
	 * delete a mood by id
	 */
	@Override
	public void delete(String id) {

		if (id == null) {
			throw new IllegalArgumentException("UUID null");
		}
		mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), Mood.class);

	}
	/**
	 * update a mood 
	 * @param mood
	 * @return
	 */
	public Mood update(Mood mood) {
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(mood.getUuid())),
				new Update().set("mood", mood.getMood()).set("comment", mood.getComment()), Mood.class);
		return mood;
	}

}
