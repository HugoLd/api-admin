package org.cap.controller;

import java.io.IOException;
import java.util.UUID;

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
	ProjectRepoImplMongo prim;
	/**
	 * try to add a project when POST on /projects
	 * if request is ok return 201 + Project Json
	 * if not ok return 400
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.CREATED)
	public String saveProjects(@RequestParam(value = "title", required = false) String title) {

		if (title != null && checkTitleNotExisting(title)) {
			Project p = new Project(title);
			prim.saveObject(p);

			return p.toJson();
		}
		throw new EmptyResultDataAccessException(0);
		
	}
	/*
	@RequestMapping(value = "/projects/{uuid}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String addEmail(@RequestParam(value = "email", required = false) String email,@PathVariable("uiid") String uuid) {

		if (email != null && checkTitleNotInProject(email) && IsAnEmail(email) ) {
			
			Project p = prim.getObjectByID(UUID.fromString(id));
			
			return p.toJson();
		}
		throw new EmptyResultDataAccessException(0);
		
	}
	*/
	private boolean IsAnEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean checkTitleNotInProject(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * check if the title is already existing in the base
	 * @param title
	 * @return
	 */
	public boolean checkTitleNotExisting(String title) {
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
