package org.cap.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.cap.bean.Mood;
import org.cap.bean.Project;
import org.cap.controller.bean.AddProjectInput;
import org.cap.controller.bean.AddUserToProjectInput;
import org.cap.controller.bean.ErrorOutput;
import org.cap.service.MoodService;
import org.cap.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


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
	@RequestMapping(value = "/projects", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Project addProject(@RequestBody AddProjectInput project) {
		return pServ.addProject(project.getTitle());
	}

	/**
	 * try to add a project when GET on /projects if request is ok return 201 +
	 * 
	 * @return list of projects
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getProjects() {
		return pServ.getProjects();

	}

	/**
	 * 
	 * @param uuid
	 * @return project
	 */
	@RequestMapping(value = "/projects/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Project getProject(@PathVariable("uuid") String uuid) {
		return pServ.getProject(uuid);
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
	@RequestMapping(value = "/projects/{uuid}/emails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Project addEmail(@PathVariable("uuid") String uuid, @RequestBody AddUserToProjectInput user) {
		return pServ.addUserToProject(uuid, user.getEmail());
	}

	/**
	 * Mapping for sending mail to all the team
	 * @param uuid
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuid}/sendMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
	@RequestMapping(value = "/projects/{uuid}/moods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mood addMood(@PathVariable("uuid") String uuid, @RequestBody Mood mood) throws EmptyResultDataAccessException {
		return mServ.saveMood(mood);
	}

	/**
	 * get mood from an user
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuid}/moods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
	@ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleEmptyResult(final Exception e, final HttpServletRequest request, Writer writer) throws IOException {
		ErrorOutput err = new ErrorOutput(e.getMessage());
		writer.write(new ObjectMapper().writeValueAsString(err));
	}

}
