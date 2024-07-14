package display;

import java.io.*;
import java.util.*;

import dbtest.*;

public class DisplayEventsBean implements Serializable {
	public List<Event> event;
	public String name;
	public String date;
	public String url;
	public String category;
	
	public List<Event> getList() {
		return event;
	}
	
	public void setList(List<Event> event) {
		this.event = event;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void makeList(int KEY) {
		SearchEvents sea = new SearchEvents();
		ProjectInfo pi = new ProjectInfo(KEY);
		this.name = pi.projectName;
		this.date = "" + pi.dateTime;
		
		UserAndProjectInfo uapi = new UserAndProjectInfo();
		UserAndProjectInfo UAPI = uapi.getVoteInfo(KEY);
		
		String category = DisplayBean.findMode(UAPI.genreList);
		setCategory(category);
		pi.category = category;
		pi.updateProjectInfo();
		
		ArrayList<Event> events = sea.search(pi.region, pi.category, pi.dateTime.toString());
		this.event = events;
	}
	
}