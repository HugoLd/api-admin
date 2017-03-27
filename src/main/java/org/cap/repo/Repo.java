package org.cap.repo;

import java.util.List;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mongodb.WriteResult;

/**
 * Interface to implements for basic Database operations
 * @author hledall
 *
 * @param <T>
 */
public interface Repo<T>{
	//public List<T> getAllObjects();

	public void saveObject(T object);

	//public T getObject(String id);

	//public WriteResult updateObject(String id, String name);

	//public void deleteObject(String id);

	//public void createCollection();

	//public void dropCollection();

}
