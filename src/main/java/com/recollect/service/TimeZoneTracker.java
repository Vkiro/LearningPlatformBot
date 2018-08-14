package com.recollect.service;

import java.util.HashMap;
import java.util.Map;

public class TimeZoneTracker {
  public static int UKRAINE_TIME_ZONE = 3;

  // TODO add more languageCodes and timezone hours
  private static final Map<String, Integer> timeZone = new HashMap<String, Integer>() {{
    put("uk-UA", UKRAINE_TIME_ZONE);
  }};

  public static int getHours(String languageCode){
    return timeZone.get(languageCode);
  }
}
