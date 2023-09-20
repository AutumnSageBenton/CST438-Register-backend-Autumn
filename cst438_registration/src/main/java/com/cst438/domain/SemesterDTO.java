package com.cst438.domain;

public record SemesterDTO 
  (int year, String semester, int courseId, int section, String title, String times, String building, String room, String instructor, String startDate, String endDate) {

}

