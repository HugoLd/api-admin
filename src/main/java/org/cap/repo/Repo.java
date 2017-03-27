package org.cap.repo;



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
	//public List<T> getAllObjects();
	/**
	 * insert a <T> object in the db
	 * @param object
	 */
	public void saveObject(T object);
	/**
	 * get a <T> object in the db by id
	 */
	//public T getObject(String id);
	/**
	 * update a <T> object in the db
	 * NEXT SPRINT
	 */
	//public WriteResult updateObject(<T> object);
	/**
	 * delete an object in the db
	 * NEXT SPRINT
	 */
	//public void deleteObject(String id);


}
