package org.cap.repo;

import java.util.List;

/**
 * Interface to implements for basic Database operations
 * @author hledall
 *
 * @param <T>
 */
public interface Repo<T>{
	/**
	 * get all the <T> objects
	 * @param object
	 */
	List<T> getAll();

	/**
	 * insert a <T> object in the db
	 * @param object
	 */
	void save(T object);

	/**
	 * get a <T> object in the db by id
	 * NEXT SPRINT
	 */
	T get(String id);

	// TODO: public void update(Project project) {
	
	/**
	 * delete an object in the db
	 *
	 */
	void delete(String id);

}
