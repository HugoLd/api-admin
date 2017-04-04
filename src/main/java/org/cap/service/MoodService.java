package org.cap.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.cap.bean.Mood;
import org.cap.repo.MoodRepoImplMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class MoodService {
	@Autowired
	MoodRepoImplMongo mrim;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * get the list of daily moods' list
	 * 
	 * @param uuid
	 * @return
	 */
	public List<List<Mood>> getProjectMoods(String uuid) throws EmptyResultDataAccessException {
		if (uuid == null) {
			throw new EmptyResultDataAccessException(0);
		}
		return sortProjectMoods(mrim.getProjectMoods(uuid));
	}

	/**
	 * sort the list extracted from DB
	 * 
	 * @param moods
	 * @return
	 */
	public List<List<Mood>> sortProjectMoods(List<Mood> moods) throws EmptyResultDataAccessException {
		checkListOk(moods);
		return browseList(moods);
	}

	/**
	 * 
	 * @param moods
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	public List<List<Mood>> browseList(List<Mood> moods) throws EmptyResultDataAccessException {
		List<List<Mood>> listList = new ArrayList<List<Mood>>();
		List<Mood> lm;
		int numList;
		for (Mood m : moods) {
			numList = searchDate(listList, m.getDate());
			if (numList != -1) {
				listList.get(numList).add(m);
			} else {
				lm = new ArrayList<Mood>();
				lm.add(m);
				listList.add(lm);
			}
		}
		return listList;
	}

	/**
	 * search existing date
	 * 
	 * @param listList
	 * @param date
	 * @return
	 */
	public int searchDate(List<List<Mood>> listList, String date) {
		for (int i = 0; i < listList.size(); i++) {
			if (listList.get(i).get(0).getDate().equals(date)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * check if the list is null or empty
	 * 
	 * @param moods
	 * @return
	 */
	private boolean checkListOk(List<Mood> moods) {
		if (moods == null || moods.isEmpty())
			throw new EmptyResultDataAccessException(0);
		return true;

	}

	/**
	 * save a mood in the DB
	 * 
	 * @param uuid
	 * @param json
	 * @return
	 */
	public Mood saveMood(String uuid, String json) throws EmptyResultDataAccessException {
		System.out.println("passed");
		System.out.println(json);
		System.out.println(getNode(json,"mood_uuid"));
		Mood m;
		String comment,uuidMood,date;
		int mood;
		if (checkSaveRequest(uuid, json)) {
			uuidMood = getNode(json, "mood_uuid");
			System.out.println(uuidMood);
			mood = Integer.parseInt(getNode(json, "mood"));
			date = getNode(json,"date");
			if (checkContainsComment(json)) {
				comment = getNode(json, "comment");
				m = new Mood(uuidMood, uuid, mood, comment, date);
			} else {
				m = new Mood(uuidMood, uuid, mood, date);
			}
			mrim.saveObject(m);
			return m;
		}

		throw new EmptyResultDataAccessException(0);
	}

	public boolean checkContainsComment(String json) {
		if (getNode(json, "comment") != null)
			return true;
		return false;
	}

	/**
	 * check if parameters are correct
	 * 
	 * @param uuid
	 * @param json
	 * @return
	 */
	public boolean checkSaveRequest(String uuid, String json) {
		if (json != null && validJson(json)) {
			if (getNode(json, "mood_uuid") != null && checkUUIDMoodNotExisting(getNode(json, "mood_uuid"))
					&& getNode(json, "mood") != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get a json node by name
	 * 
	 * @param json
	 * @param var
	 * @return
	 */
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
	 * check if the uuid already exist in the DB
	 * 
	 * @param uuid
	 * @return
	 */
	protected boolean checkUUIDMoodNotExisting(String uuid) {
		Mood p = mrim.getObject(uuid);
		if (p == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return the today's date
	 */
	public String getDateNow() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(date);
	}

}
