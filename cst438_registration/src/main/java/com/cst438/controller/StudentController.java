package com.cst438.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.SemesterDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
@RestController
@CrossOrigin 

public class StudentController {
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	/*
	 * get major offerings 
	 */
	@GetMapping("/major")
	public SemesterDTO[] getSchedule( @RequestParam("year") int year, @RequestParam("semester") String semester, @RequestParam("title") String title ) {
		System.out.println("/major called.");
		String course_title = "CST";
		
		Course course = courseRepository.findByTitle(course_title);
		if (course != null) {
//			System.out.println("/major student "+course.getName()+" "+student.getStudent_id());
			List<Course> courses = courseRepository.findStudentSchedule(course_title, year, semester);
			SemesterDTO[] sched = createSemester(year, semester, course, courses);
			return sched;
		} else {
			return new SemesterDTO[0];   // return empty schedule for unknown student.
		}
	}
	
	
	private SemesterDTO[] createSemester(int year, String semester, Course c, List<Course> courses) {
		SemesterDTO[] result = new SemesterDTO[courses.size()];
		for (int i=0; i<courses.size(); i++) {
			SemesterDTO dto =createSemester(courses.get(i));
			result[i] = dto;
		}
		return result;
	}
	
	private SemesterDTO createSemester(Course c) {
		SemesterDTO dto = new SemesterDTO(
		   c.getYear(),
		   c.getSemester(),
		   c.getCourse_id(),
		   c.getSection(),
		   c.getTitle(),
		   c.getTimes(),
		   c.getBuilding(),
		   c.getRoom(),
		   c.getInstructor(),
		   c.getStart().toString(),
		   c.getEnd().toString());
		   
		return dto;
	}
	
	
}
