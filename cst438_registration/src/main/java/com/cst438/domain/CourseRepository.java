package com.cst438.domain;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface CourseRepository extends CrudRepository <Course, Integer> {
	Course findByTitleLike(String course);
	
	@Query("select e from Course e where e.title=:title and e.year=:year and e.semester=:semester")
	public List<Course> findSchedule(
			@Param("title") String title, 
			@Param("year") int year, 
			@Param("semester") String semester);
	
//	Course findById(int id);

}