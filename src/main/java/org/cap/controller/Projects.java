package org.cap.controller;

import java.io.IOException;
import java.util.List;

import org.cap.bean.Project;
import org.cap.service.ProjectService;
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
 * 
 * @author hledall
 *
 */
@RestController
public class Projects {
	@Autowired
	protected ProjectService pServ;

	/**
	 * try to add a project when POST on /projects if request is ok return 201 +
	 * Project Json if not ok return 400
	 * 
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.CREATED)
	public Project saveProjects(@RequestParam(value = "title", required = false) String title) {
		return pServ.saveProjects(title);
	}

	/**
	 * try to add an email to a project document in mongo when POST /projects/{uuid} 
	 * 
	 * @param email
	 * @param uuid
	 * @return project
	 */
	@RequestMapping(value = "/projects/{uuid}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public Project addEmail(@RequestParam(value = "email", required = false) String email,
			@PathVariable("uuid") String uuid) {
		return pServ.addEmail(email, uuid);

	}
	/**
	 * try to add a project when GET on /projects if request is ok return 201 +
	 * @return list of projects
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getProjects() {
		return pServ.getProjects();

	}
	/**
	 * 
	 * @param uuid
	 * @return project
	 */
	@RequestMapping(value = "/projects/{uuid}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public Project getProject(@PathVariable("uuid") String uuid) {
		return pServ.getProject(uuid);

	}

	

	/**
	 * to return 400 on error
	 * 
	 * @param e
	 * @throws IOException
	 */

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleEmptyResult(Exception e) throws IOException {
	}
}
