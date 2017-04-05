package org.cap.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	public Project addProject(String title) {
		// validation
		if (null == title || title.matches("^[ \t]*$")) {
			throw new IllegalArgumentException("title ko");
		}
		if (isTitleAlreadyExists(title)) {
			throw new IllegalArgumentException("title existant");
		}

		// sauvegarde
		Project project = new Project();
		project.setId(UUID.randomUUID().toString());
		project.setTitle(title);
		prim.save(project);
		return project;
	}

	public Project addUserToProject(String projectUUID, String email) {
		if (! isAnEmail(email)) {
			throw new IllegalArgumentException("email invalid");
		}
		Project project = prim.get(projectUUID);
		if (null == project) {
			throw new IllegalArgumentException("projet non trouve");
		}

		if (! project.getMails().contains(email)) {
			project.getMails().add(email);
			prim.update(project);
			// on pourrait imaginer utilise le $push de mongo plutot que de reupdate tout le client
		}
		return project;
	}

	
	/**
	 * try to add a project when POST on /projects if request is ok return 201 +
	 * Project Json if not ok return 400
	 * 
	 * @param title
	 * @return
	 * @throws IllegalArgumentException si param KO
	 */
	public Project saveProject(Project project) {
		// validation
		if (null == project.getTitle() || project.getTitle().matches("^[ \t]*$")) {
			throw new IllegalArgumentException("title ko");
		}
		if (isTitleAlreadyExists(project.getTitle())) {
			throw new IllegalArgumentException("title existant");
		}

		// sauvegarde
		prim.save(project);
		return project;
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
		Project p = prim.get(uuid);
		List<String> listMail;
		if (p != null && email != null && isAnEmail(email)) {
			listMail = p.getMails();
			if (!p.getMails().contains(email)) {
				listMail.add(email);
				prim.delete(uuid);
				prim.save(p);
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
	protected boolean isAnEmail(String email) {
		// TODO si email null
		// TODO: plutot utiliser une regex sur stackoverflow qui va vraiment faire les cas de tests :p
		if (email.split("@").length == 2 && email.length() > 6 && email.length() < 30 && email.contains(".")) {
			return true;
		}
		return false;
	}

	/**
	 * check if the title is already existing in the base
	 * 
	 * @param title
	 * @return true si le title exists en base
	 */
	public boolean isTitleAlreadyExists(String title) {
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
	 */
	public void sendMail(String uuid) {
		Project project = prim.get(uuid);
		System.out.println(project.getTitle());
		// TODO: check uuid a faire avant la recuperation - ou ne pas faire car ici nous avons deja le project
		if (uuid != null && project != null && ms.checkProperties()) {
			try {
				final Map<String, Object> props = new HashMap<String, Object>();
				// TOOD: appeler un chat un chat. ms on ne sait pas a quoi ca correspond : mailService ca mange pas de pain !
				props.put("date", ms.getDateNowWithDayOfWeek());
				String date = ms.getDateNow();
				for (String mail : project.getMails()) {
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
