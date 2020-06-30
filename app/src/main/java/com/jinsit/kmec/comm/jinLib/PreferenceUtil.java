package com.jinsit.kmec.comm.jinLib;

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil
{
   private static PreferenceUtil _instance = null;
 
   private static final String PROPERTY_REG_ID = "registration_id";
   private static final String PROPERTY_APP_VERSION = "appVersion";
   private static final String PROPERTY_NOTICE = "noticeCount";
   private static final String PROPERTY_SAFE_DRIVE_NOTICE = "safeDriveNotice";
   private static final String PROPERTY_JOB_SORT = "jobSort";
   private static final String PROPERTY_SORT_ORDERBY_ASC = "orderbyASC";
   public static synchronized PreferenceUtil instance(Context $context)
   {
      if (_instance == null)
         _instance = new PreferenceUtil($context);
      return _instance;
   }
 
   public PreferenceUtil(Context $context)
   {
      super($context);
   }
 
   public void putRedId(String $regId)
   {
      put(PROPERTY_REG_ID, $regId);
   }
 
   public String regId()
   {
      return get(PROPERTY_REG_ID);
   }
 
   public void putAppVersion(int $appVersion)
   {
      put(PROPERTY_APP_VERSION, $appVersion);
   }
 
   public int appVersion()
   {
      return get(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
   }
   
   
   public void putNoticeCount(int $noticeCount)
   {
	   put(PROPERTY_NOTICE, $noticeCount);
   }
   
   public int getNoticeCount()
   {
	   return get(PROPERTY_NOTICE, Integer.MIN_VALUE);
   }
   
   public void putSafeDriveNoticeCount(int $count){
	   put(PROPERTY_SAFE_DRIVE_NOTICE, $count+1);
   }
   public int getSafeDriveNoticeCount(){
	   
	   return get(PROPERTY_SAFE_DRIVE_NOTICE, 1);
   }



   public void putJobSort(String sort)
   {
      put(PROPERTY_JOB_SORT, sort);
   }

   public String getJobSort()
   {
      return get(PROPERTY_JOB_SORT);
   }

   public void putOrderByASC(boolean orderBy){
      put(PROPERTY_SORT_ORDERBY_ASC, orderBy);
   }
   public boolean getOrderByASC(){
      return get(PROPERTY_SORT_ORDERBY_ASC, false);
   }




}