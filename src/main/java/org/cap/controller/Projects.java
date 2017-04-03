package org.cap.controller;

import java.io.IOException;
import java.util.List;

import org.cap.bean.Mood;
import org.cap.bean.Project;
import org.cap.service.MoodService;
import org.cap.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


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
	@Autowired
	protected MoodService mServ;

	/**
	 * try to add a project when POST on /projects if request is ok return 201 +
	 * Project Json if not ok return 400
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.CREATED)
	public Project saveProjects(@RequestBody(required = false) String json)
			throws JsonParseException, JsonMappingException, IOException {

		return pServ.saveProjects(json);
	}

	/**
	 * try to add an email to a project document in mongo when POST
	 * /projects/{uuid}
	 * 
	 * @param email
	 * @param uuid
	 * @return project
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuid}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public Project addEmail(@RequestBody(required = false) String json, @PathVariable("uuid") String uuid)
			throws EmptyResultDataAccessException, JsonParseException, JsonMappingException, IOException {
		return pServ.addEmail(json, uuid);

	}

	/**
	 * try to add a project when GET on /projects if request is ok return 201 +
	 * 
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
	 * Mapping for sending mail to all the team
	 * @param uuid
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuid}/sendMail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public void sendMail(@PathVariable("uuid") String uuid) throws EmptyResultDataAccessException {

		pServ.sendMail(uuid);

	}
	/**
	 * get mood from an user
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuid}/userMood", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public Mood addMood(@PathVariable("uuid") String uuid,@RequestBody(required = false)String json) throws EmptyResultDataAccessException {
		return mServ.saveMood(uuid , json);

	}

	/**
	 * get mood from an user
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuid}/moods", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public List<List<Mood>> getProjectMoods(@PathVariable("uuid") String uuid) throws EmptyResultDataAccessException {
		return mServ.getProjectMoods(uuid);
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
