package com.recollect.dao;

import java.time.LocalTime;

public class Main {

  public static void main(String[] args) {
    System.out.println(LocalTime.now().getHour() + 3 > 24 ? LocalTime.now().getHour() + 3 - 24
        : LocalTime.now().getHour() + 3); //KIEV time zone
  }
}
