package org.cap.service;

import java.util.ArrayList;
import java.util.List;

import org.cap.bean.Mood;
import org.cap.repo.MoodRepoImplMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	public Mood saveMood(Mood m) throws EmptyResultDataAccessException {
		try{
			mrim.save(m);
			return m;
		}
		catch(Exception e){
			throw new EmptyResultDataAccessException(0);
		}
		
	}

	
	/**
	 * check if the uuid already exist in the DB
	 * 
	 * @param uuid
	 * @return
	 */
	protected boolean checkUUIDMoodNotExisting(String uuid) {
		Mood p = mrim.get(uuid);
		if (p == null) {
			return true;
		}
		return false;
	}


}
