package com.cst438.service;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	private static String gradebook_url;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	CourseRepository courseRepository;

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		System.out.println("Start Message "+ student_email +" " + course_id); 

		Student student = studentRepository.findByEmail(student_email);
		Course course  = courseRepository.findById(course_id).orElse(null);
		Enrollment enroll = enrollmentRepository.findByEmailAndCourseId(student_email, course_id);
		
		// TODO use RestTemplate to send message to gradebook service
		RestTemplate restTemplate = new RestTemplate();		
		ResponseEntity<EnrollmentDTO> response = 
                       restTemplate.postForEntity(
                           gradebook_url, 
                           enroll, 
                           EnrollmentDTO.class);
		if (response.getStatusCodeValue() == 200) {
			// update database
			EnrollmentDTO enr = response.getBody();
			Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(
                                        enr.studentEmail(), enr.courseId());
			enrollmentRepository.save(enrollment);		
		} else {
			// error.
			System.out.println(
                         "Error: unable to post gradebook"+
                          response.getStatusCodeValue());
		}

		
		
	}
	
	/*
	 * endpoint for final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
		System.out.println("Grades received "+grades.length);
		
		//TODO update grades in enrollment records with grades received from gradebook service		
		for(int i = 0; i < grades.length; i++) {
			Enrollment e2 = enrollmentRepository.findByEmailAndCourseId(grades[i].studentEmail(), course_id);
			e2.setCourseGrade(grades[i].grade());
			enrollmentRepository.save(e2);
		}	
	}
}
