package org.cap.controller;

import java.io.IOException;
import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller on /projects call
 * @author hledall
 *
 */
@RestController
public class Projects {
	@Autowired
	protected ProjectRepoImplMongo prim;
	/**
	 * try to add a project when POST on /projects
	 * if request is ok return 201 + Project Json
	 * if not ok return 400
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.CREATED)
	public Project saveProjects(@RequestParam(value = "title", required = false) String title) {

		if (title != null && checkTitleNotExisting(title)) {
			Project p = new Project(title);
			prim.saveObject(p);

			return p;
		}
		throw new EmptyResultDataAccessException(0);
		
	}
	
	@RequestMapping(value = "/projects/{uuid}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Project addEmail(@RequestParam(value = "email", required = false) String email,@PathVariable("uuid") String uuid) {
	
		if (email != null && checkEmailNotInProject(email) && IsAnEmail(email) ) {
			Project p = prim.getObjectByID(uuid);
			p.addToList(email);
			prim.deleteObject(uuid);
			prim.saveObject(p);
			return p;
		}
		throw new EmptyResultDataAccessException(0);
	
		
	}
	
	/**
	 * check if it seems to be a real email
	 * @param email
	 * @return
	 */
	protected boolean IsAnEmail(String email) {
		if(email.split("@").length == 2 && email.length()>6 && email.length() < 30 && email.contains(".")){
			return true;
		}
		return false;
	}
	/**
	 * check that the email is not already existing in the project
	 * @param email
	 * @return boolean
	 */
	protected boolean checkEmailNotInProject(String email) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * check if the title is already existing in the base
	 * @param title
	 * @return
	 */
	protected boolean checkTitleNotExisting(String title) {
		Project p = prim.getObjectByTitle(title);
		if(p == null){
			return true;
		}
		return false;
	}
	/**
	 * to return 400 on error
	 * @param e
	 * @throws IOException
	 */

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleEmptyResult(Exception e) throws IOException {
	}
}
