package com.recollect.service;

import java.util.HashMap;
import java.util.Map;

public class TimeZoneTracker {

  // TODO add more languageCodes and timezone hours
  private static final Map<String, Integer> timeZone = new HashMap<>() {{
    put("uk-UA", 3);
  }};

  public static int getHours(String languageCode){
    return timeZone.get(languageCode);
  }
}
