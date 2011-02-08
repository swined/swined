package com.excelsior.lms.licencing;

public class LicenceManager {

  public static boolean isFeatureEnabled(LmsFeature feature){
    return true;
  }
  
  public static int maxRunningJobsAllowed() {
    return Integer.MAX_VALUE;
  }

  public static String readRawLicenseState() {
    return "<fake license>";
  }  

}