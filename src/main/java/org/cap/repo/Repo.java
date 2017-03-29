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
	public List<T> getAllObjects();
	/**
	 * insert a <T> object in the db
	 * @param object
	 */
	public void saveObject(T object);
	/**
	 * get a <T> object in the db by id
	 * NEXT SPRINT
	 */
	public T getObject(String id);
	/**
	 * delete an object in the db
	 *
	 */
	public void deleteObject(String id);


}
