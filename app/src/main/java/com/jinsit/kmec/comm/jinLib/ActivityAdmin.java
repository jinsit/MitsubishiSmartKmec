package com.jinsit.kmec.comm.jinLib;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityAdmin {

	//FIELD
	private static ActivityAdmin ACTIVITY_MANAGER = null;
	private static List<Activity> ACTIVITY_STORE = null;
	private static List<Activity> ACTIVITY_MENU = null;

	//CONSTRUCTOR
	public ActivityAdmin(){
		if(ACTIVITY_STORE == null){
			ACTIVITY_STORE = new ArrayList<Activity>();
		}
		if(ACTIVITY_MENU == null){
			ACTIVITY_MENU = new ArrayList<Activity>();
		}
	}
	
	//to return the an instance of this
	public static ActivityAdmin getInstance(){
		return ACTIVITY_MANAGER == null ? new ActivityAdmin() : ACTIVITY_MANAGER;
	}
	
	
	//[1]
	//to add an activity in ArrayList
	public boolean addActivity(Activity activity){
		return ACTIVITY_STORE.add(activity);
	}
	//to remove an activity in ArrayList
	public boolean finishActivity(Activity activity){
		return ACTIVITY_STORE.remove(activity);
	}
	//to get rid of all the activities from ArrayList
	public void finishAllActivities(){
		for(Activity activity : ACTIVITY_STORE){
			activity.finish();
		}
		ACTIVITY_STORE.clear();
	}
	public int getActivitySize(){
		return ACTIVITY_STORE.size();
	}	
	
	
	//[2]
	//to add a menu-activity 
	public boolean addMenuActivity(Activity activity){
		return ACTIVITY_MENU.add(activity);
	}
	public boolean finishMenuActivity(Activity activity){
		return ACTIVITY_MENU.remove(activity);
	}
	public void finishLastMenuActivites(){
		for(Activity activity : ACTIVITY_MENU){
			activity.finish();
		}
		ACTIVITY_MENU.clear();
	}
	public List getMenuActivity(){
		return ACTIVITY_MENU;
	}

	public static void setActMovementSys(Activity activity){
		ActivityAdmin.getInstance().finishAllActivities();
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(activity);
		ActivityAdmin.getInstance().addActivity(activity);
	}
	
};