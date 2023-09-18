package com.cst438.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends CrudRepository <Course, Integer> {
	Course findByTitle(String title);
	
	@Query("select e from Course e where e.course.title=:title and e.year=:year and e.semester=:semester")
	public List<Course> findStudentSchedule(
			@Param("title") String title, 
			@Param("year") int year, 
			@Param("semester") String semester);
	
}