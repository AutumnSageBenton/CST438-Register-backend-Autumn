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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.SemesterDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
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
		String course_title = "CST 237%";
		
		Course course = courseRepository.findByTitleLike(course_title);

		if (course != null) {
			List<Course> courses = courseRepository.findSchedule(course.getTitle(), year, semester);
			SemesterDTO[] sched = createSemester(year, semester, course, courses);
			return sched;
		} else {
			return new SemesterDTO[0];   // return empty schedule for unknown student.
		}
	}
	
	/*
	 * get current list of students
	 */
	@GetMapping("/student")
	public StudentDTO[] getStudent( @RequestParam("email") String email ) {
//		System.out.println("/schedule called.");
//		String student_email = "test@csumb.edu";   // student's email 
		
		Student student = studentRepository.findByEmail(email);
		if (student != null) {
//			System.out.println("/schedule student "+student.getName()+" "+student.getStudent_id());
//			Student students = studentRepository.findByEmail(student_email);
			StudentDTO[] sched = createStudent(student.getStudent_id(), student.getName(), email, student);
			return sched;
		} else {
			return new StudentDTO[0];   // return empty schedule for unknown student.
		}
	}
	
	/*
	 * add a student 
	 */
	@PostMapping("/students/{name}/{email}/{id}")
	@Transactional
	public StudentDTO addStudent( @PathVariable String name, @PathVariable String email, @PathVariable int id  ) { 
		Student student = studentRepository.findByEmail(email);
		// verify student doesn't exist	
		if (student == null) {
			Student s = new Student(name, email);
			s.setStatus(null);
			s.setStatusCode(0);
			s.setStudent_id(id);
			StudentDTO result = createStudent(s);
			
			return result;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "student already exists");
		}	
	}
	
	/*
	 * drop a student
	 */
	@DeleteMapping("/students/{id}")
	@Transactional
	public void dropCourse(  @PathVariable int id  ) {
		Student s = studentRepository.findById(id).orElse(null);
		
		// verify that student exists
		if (s != null) {
			 studentRepository.delete(s);
		} else {
			// something is not right with the enrollment.  
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "student does not exist");
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
	

	private StudentDTO[] createStudent(int id, String name, String email, Student s) {
		StudentDTO[] result = new StudentDTO[1];
		StudentDTO dto = createStudent(s);
		result[0] = dto;
		return result;
	}
	
	private StudentDTO createStudent(Student s) {
		StudentDTO dto = new StudentDTO(
		   s.getStudent_id(), 
		   s.getName(), 
		   s.getEmail(), 
		   s.getStatus(), 
		   s.getStatusCode()
				);
		   
		return dto;
	}

	
	
}
