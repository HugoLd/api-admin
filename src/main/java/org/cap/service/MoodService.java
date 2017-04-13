package org.cap.service;

import java.util.ArrayList;
import java.util.List;

import org.cap.bean.Mood;
import org.cap.bean.MoodValue;
import org.cap.bean.Project;
import org.cap.repo.MoodRepoImplMongo;
import org.cap.repo.ProjectRepoImplMongo;
import org.cap.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MoodService {
	@Autowired
	MoodRepoImplMongo mrim;
	@Autowired
	ProjectRepoImplMongo prim;
	Util util;

	/**
	 * get the list of daily moods' list
	 * 
	 * @param uuid
	 * @return
	 */
	public List<List<Mood>> getProjectMoods(String uuid) {
		if (uuid == null) {
			throw new IllegalArgumentException("Project UUID is null");
		}
		return sortProjectMoods(mrim.getProjectMoods(uuid));
	}

	/**
	 * sort the list extracted from DB
	 * 
	 * @param moods
	 * @return
	 */
	private List<List<Mood>> sortProjectMoods(List<Mood> moods) {
		checkListOk(moods);
		return browseList(moods);
	}

	/**
	 * browse the list to return moods by day
	 * @param moods
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	private List<List<Mood>> browseList(List<Mood> moods) {
		List<List<Mood>> listList = new ArrayList<List<Mood>>();
		List<Mood> lm;
		int numList;
		for (Mood m : moods) { 
			numList = searchDate(listList, m.getDate()); // get the mood for a date order by date
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
	private int searchDate(List<List<Mood>> listList, String date) {
		for (int i = 0; i < listList.size(); i++) {
			if (listList.get(i).get(0) != null) {
				if (listList.get(i).get(0).getDate() != null) {
					if (listList.get(i).get(0).getDate().equals(date)) {
						return i;
					}
				}
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
			throw new IllegalArgumentException("List is null or empty");
		return true;

	}

	/**
	 * save a mood in the DB
	 * 
	 * @param uuid
	 * @param json
	 * @return
	 */
	public Mood saveMood(Mood m) {
		if (checkMoodOk(m)) {
			mrim.save(m);
			return m;
		}
		throw new IllegalArgumentException("Error saving the mood");

	}

	/**
	 * check if all the fields are ok
	 * 
	 * @param m
	 * @return
	 */
	private boolean checkMoodOk(Mood mood) {
		return (mood.getDate() != null && mood.getUuid() != null && mood.getUuidProj() != null
				&& MoodValue.getEnumValue(mood.getMood()) != null && checkNoCheat(mood));
	}

	/**
	 * check if the uuid seems can be a real uuid
	 * 
	 * @param mood
	 * @return
	 */
	private boolean checkNoCheat(Mood mood) {
		
		Project proj = prim.get(mood.getUuidProj());
		for (String mail : proj.getMails()) {
			if (Util.generateUUID(proj.getId(), mail, mood.getDate()).equals(mood.getUuid())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * remove all old moods ( >3 months)
	 * @param uuidProj
	 */
	public void purgeProj(String uuidProj) {
		List<Mood> listMood = mrim.getProjectMoods(uuidProj);
		int dayNowCount= getTotalDayAmount(Util.getDateNow().split("-"));
		for(Mood m : listMood){
			System.out.println(m.getUuid());
			if(m.getDate().split("-").length != 3 ){
				mrim.delete(m.getUuid());
			}
			if((dayNowCount - getTotalDayAmount(m.getDate().split("-")))>90){
				mrim.delete(m.getUuid());
			}
			if((dayNowCount - getTotalDayAmount(m.getDate().split("-")))<0){
				mrim.delete(m.getUuid());
			}			
		}
	}

	
	/**
	 * get amount of days from a String[]
	 * @param tabString
	 * @return
	 */
	private int getTotalDayAmount(String[] tabString){
		int[] tabInt = new int[tabString.length];
		for(int i = 0 ; i<tabString.length ; i++){
			tabInt[i] = Integer.parseInt(tabString[i]);
		}
		return tabInt[0] + (tabInt[1]-1*30) + (tabInt[2]-1 * 365); 
	}
	
}
