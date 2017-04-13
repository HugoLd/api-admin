package org.cap.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.cap.bean.Mood;
import org.cap.bean.Project;
import org.cap.controller.bean.AddMoodInput;
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

import freemarker.template.TemplateException;

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
	 * 
	 * controller /projects getting the projects' list ( doesn't include mails)
	 * @return list of projects
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getProjects() {
		return pServ.getProjects();

	}
	/**
	 * controller /allProjects getting the projects' list including mails
	 * @return complete list of projects
	 */
	@RequestMapping(value = "/allProjects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getAllProjects() {
		return pServ.getAllProjects();

	}

	/**
	 * url for getting a project
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
	 * 
	 * @param uuid
	 * @throws TemplateException 
	 * @throws IOException 
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuidProj}/sendMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void sendMail(@PathVariable("uuidProj") String uuid) throws IOException, TemplateException {
		pServ.sendMail(uuid);
	}

	/**
	 * Mapping for sending mail to all the team
	 * 
	 * @param uuid
	 * @throws TemplateException 
	 * @throws IOException 
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/sendMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void sendMailAll() throws IOException, TemplateException {
		pServ.sendMailAll();
	}
	/**
	 * get mood from an user
	 * 
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuidProj}/moods", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mood addMood(@PathVariable("uuidProj") String uuidProj, @RequestBody AddMoodInput mood) {
		mood.setUuidProj(uuidProj);
		Mood moodToSave = new Mood(mood.getUuid(), uuidProj, mood.getMood(), mood.getComment(), mood.getDate());
		return mServ.saveMood(moodToSave);
	}
	
	/**
	 * delete a project from the DB
	 * 
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuidProj}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteProject(@PathVariable("uuidProj") String uuidProj) {	
		pServ.deleteProj(uuidProj);
	}
	
	
	/**
	 * get mood from an user
	 * 
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuidProj}/deleteUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("uuidProj") String uuidProj, @RequestBody AddUserToProjectInput user) {
		pServ.deleteUser(user.getEmail() ,uuidProj);
	}

	/**
	 * get mood from an user
	 * 
	 * @param uuid
	 * @param json
	 * @throws EmptyResultDataAccessException
	 */
	@RequestMapping(value = "/projects/{uuidProj}/moods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<List<Mood>> getProjectMoods(@PathVariable("uuidProj") String uuid) {
		return mServ.getProjectMoods(uuid);
	}

	/**
	 * to return 400 on error
	 * 
	 * @param e
	 * @throws IOException
	 */
	@ExceptionHandler({ IllegalArgumentException.class, HttpMessageNotReadableException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public void handleEmptyResult(final Exception e, final HttpServletRequest request, Writer writer)
			throws IOException {
		ErrorOutput err = new ErrorOutput(e.getMessage());
		writer.write(new ObjectMapper().writeValueAsString(err));
	}

}
