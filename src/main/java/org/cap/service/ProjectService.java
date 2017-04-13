package org.cap.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.cap.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.TemplateException;

@Service
public class ProjectService {
	@Autowired
	protected ProjectRepoImplMongo prim;
	@Autowired
	protected MailService mailService;
	ObjectMapper mapper = new ObjectMapper();
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);


	/**
	 * add a project to the DB
	 * @param title
	 * @return
	 */
	public Project addProject(String title) {
		if (null == title || !title.matches("^[a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*$")) {
			throw new IllegalArgumentException("incorrect project title");
		}
		if (isTitleAlreadyExists(title)) {
			throw new IllegalArgumentException("title already exist");
		}

		Project project = new Project();
		project.setId(UUID.randomUUID().toString());
		project.setTitle(title);
		prim.save(project);
		return project;
	}
	/**
	 * add an e mail to the project
	 * @param projectUUID
	 * @param email
	 * @return
	 */
	public Project addUserToProject(String projectUUID, String email) {
		if (!isAnEmail(email)) {
			throw new IllegalArgumentException("invalid email");
		}
		Project project = prim.get(projectUUID);
		if (null == project) {
			throw new IllegalArgumentException("project not found");
		}

		if (!project.getMails().contains(email)) {
			project.getMails().add(email);
			prim.update(project);
		}
		return project;
	}

	/**
	 * check if it match an e mail pattern
	 * 
	 * @param email
	 * @return
	 */
	private boolean isAnEmail(String email) {
		return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
	}

	/**
	 * check if the title is already existing in the base
	 * 
	 * @param title
	 * @return true si le title exists en base
	 */
	private boolean isTitleAlreadyExists(String title) {
		return prim.getObjectByTitle(title) != null;
	}

	/**
	 * try to add a project when GET on /projects if request is ok return 201 +
	 * 
	 * @return list of projects
	 */
	public List<Project> getProjects() {
		return prim.getAll();

	}
	/**
	 * try to add a project when GET on /projects if request is ok return 201 +
	 * 
	 * @return list of projects
	 */
	public List<Project> getAllProjects() {
		return prim.getAllWithMails();

	}

	/**
	 * Get a project with its id
	 * @param uuid
	 * @return project
	 */
	public Project getProject(String uuid) {
		return prim.get(uuid);

	}

	/**
	 * send all the mail in the project with given uuid
	 * 
	 * @param uuid
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public boolean sendMail(String uuid) throws IOException, TemplateException {
		Project project = prim.get(uuid);
		if (project == null) {
			throw new IllegalArgumentException("UUID or project null");
		}
		if (!mailService.checkProperties()) {
			throw new IllegalArgumentException("Bad properties");
		}

		final Map<String, Object> props = new HashMap<String, Object>();
		props.put("date", Util.getDateNowWithDayOfWeek());
		String date = Util.getDateNow();
		for (String mail : project.getMails()) {
			props.put("url", mailService.generateLinks(uuid, mail, date));
			props.put("projectName", project.getTitle());
			mailService.sendEmail(props, mail);
		}
		return true;
	}
	/**
	 * send the mail to everyone
	 * 
	 * @param uuid
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void sendMailAll() throws IOException, TemplateException {
		List<Project> listProj = prim.getAll();
		for(Project proj : listProj){			
			sendMail(proj.getId());
		}
	}
	/**
	 * delete a project
	 * @param uuidProj
	 */
	public void deleteProj(String uuidProj) {
		prim.delete(uuidProj);
	}
	/**
	 * remove an user from a project
	 * @param email
	 * @param uuidProj
	 */
	public void deleteUser(String email, String uuidProj) {
		prim.deleteUser(email,uuidProj);
	}
	


}
