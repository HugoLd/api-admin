package org.cap.service;

import java.util.List;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
	@Autowired
	protected ProjectRepoImplMongo prim;
	
	
	
	
	/**
	 * try to add a project when POST on /projects if request is ok return 201 +
	 * Project Json if not ok return 400
	 * 
	 * @param title
	 * @return
	 */	
	public Project saveProjects(String title) throws EmptyResultDataAccessException {

		if (title != null && checkTitleNotExisting(title)) {
			Project p = new Project(title);
			prim.saveObject(p);

			return p;
		}
		throw new EmptyResultDataAccessException(0);

	}
	
	
	/**
	 * try to add an email to a project document in mongo when POST /projects/{uuid} 
	 * 
	 * @param email
	 * @param uuid
	 * @return project
	 */
	public Project addEmail(String email,String uuid) throws EmptyResultDataAccessException{
		Project p = prim.getObject(uuid);
		List<String> listMail ;
		if (p != null && email != null && IsAnEmail(email)) {
			listMail = p.getMails();
			if (!p.getMails().contains(email)) {
				listMail.add(email);
				prim.deleteObject(uuid);
				prim.saveObject(p);
			}
			return p;
		}
		throw new EmptyResultDataAccessException(0);

	}
	
	/**
	 * check if it seems to be a real email
	 * 
	 * @param email
	 * @return
	 */
	protected boolean IsAnEmail(String email) {
		if (email.split("@").length == 2 && email.length() > 6 && email.length() < 30 && email.contains(".")) {
			return true;
		}
		return false;
	}

	/**
	 * check if the title is already existing in the base
	 * 
	 * @param title
	 * @return
	 */
	protected boolean checkTitleNotExisting(String title) {
		Project p = prim.getObjectByTitle(title);
		if (p == null) {
			return true;
		}
		return false;
	}
	/**
	 * try to add a project when GET on /projects if request is ok return 201 +
	 * @return list of projects
	 */
	public List<Project> getProjects() {
		return prim.getAllObjects();

	}
	/** 
	 * @param uuid
	 * @return project
	 */
	public Project getProject(String uuid) {
		return prim.getObject(uuid);

	}
}
