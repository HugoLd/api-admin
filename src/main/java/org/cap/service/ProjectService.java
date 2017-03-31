package org.cap.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.cap.bean.Project;
import org.cap.repo.ProjectRepoImplMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import freemarker.template.TemplateException;

@Service
public class ProjectService {
	@Autowired
	protected ProjectRepoImplMongo prim;
	@Autowired
	protected MailService ms;
	ObjectMapper mapper = new ObjectMapper();

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
	public Project saveProjects(String json)
			throws EmptyResultDataAccessException, JsonParseException, JsonMappingException, IOException {
		if (validJson(json)) {
			String title = getNode(json, "title");
			if (checkTitleNotExisting(title) && title != null) {
				Project p = new Project(title);
				prim.saveObject(p);
				return p;
			}
		}

		throw new EmptyResultDataAccessException(0);

	}

	public String getNode(String json, String var) {
		String retrn = null;
		try {
			if (json != null && validJson(json)) {
				ObjectNode node = mapper.readValue(json, ObjectNode.class);

				if (node.get(var) != null) {
					retrn = node.get(var).textValue();
					node.remove(var);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retrn;
	}

	/**
	 * validate a json
	 * 
	 * @param jsonInString
	 * @return
	 */
	public boolean validJson(String json) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(json);
			return true;
		} catch (Exception e) {
			return false;
		}
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
	 */
	public Project addEmail(String json, String uuid)
			throws EmptyResultDataAccessException, JsonParseException, JsonMappingException, IOException {
		String email = getNode(json, "email");
		Project p = prim.getObject(uuid);
		List<String> listMail;
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
	 * 
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

	/**
	 * send all the mail in the project with given uuid
	 * 
	 * @param uuid
	 */
	public void sendMail(String uuid) {
		Project p = prim.getObject(uuid);

		HashMap<String, Object> props;
		List<String> listMail;
		if (uuid != null && p != null && ms.checkProperties()) {
			try {
				props = new HashMap<String, Object>();
				listMail = p.getMails();
				props.put("date", ms.getDateNowWithDayOfWeek());
				String date = ms.getDateNow();
				for (String mail : listMail) {
					props.put("url", ms.generateLinks(uuid, mail, date));
					ms.sendEmail(props, mail);
				}
			} catch (IOException | TemplateException e) {
				throw new EmptyResultDataAccessException(0);
			}
		} else {
			throw new EmptyResultDataAccessException(0);
		}
	}

}
