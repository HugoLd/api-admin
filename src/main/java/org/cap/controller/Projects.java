package org.cap.controller;

import java.io.IOException;


import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Projects {
	@Autowired
	ProjectRepoImplMongo prim;

	/*
	 * @RequestMapping(value = "/projects", method = RequestMethod.GET) public
	 * void returnProjects() { System.out.println("/projects"); }
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.CREATED)
	public String returnProjects(@RequestParam(value = "title", required = false) String title) {

		System.out.println("/projects");
		if (title != null && checkTitleNotExisting(title)) {
			Project p = new Project(title);
			prim.saveObject(p);

			return p.toJson();
		}
		throw new EmptyResultDataAccessException(0);
		
	}

	public boolean checkTitleNotExisting(String title) {
		Project p = prim.getObjectByTitle(title);
		if(p == null){
			return true;
		}
		return false;
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleEmptyResult(Exception e) throws IOException {
	}
}
