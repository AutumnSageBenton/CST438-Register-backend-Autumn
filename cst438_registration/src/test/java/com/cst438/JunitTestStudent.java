package com.cst438;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.SemesterDTO;
import com.cst438.domain.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class JunitTestStudent {
	
	@Autowired
	private MockMvc mvc;
	
	/*
	 * find student by email
	 */
	@Test
	public void findStudent() throws Exception {
		MockHttpServletResponse response;

		response = mvc.perform( MockMvcRequestBuilders
			      .get("/student?email=test@csumb.edu")
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
				
		StudentDTO[] dto_list = fromJsonString(response.getContentAsString(), StudentDTO[].class);
		
		boolean found = false;
		for (StudentDTO dto : dto_list) {
			if (dto.email().contains("test@csumb.edu")) found=true;
		}
		assertTrue(found);
	}
	
	/* 
	 * add a student 4347, Bob Builder, bbuilder@csumb.edu
	 */
	@Test
	public void addStudent() throws Exception {
	
		MockHttpServletResponse response;

		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/students/BobBuilder/bbuilder@csumb.edu/4347")
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
		
		// verify that returned data has non zero primary key
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( null  , result);
				
		if(result.email().contains("bbuilder@csumb.edu") ){
			assertTrue(true);
		} else {
			assertTrue(false);
		}
		
	}
	
	
	/*
	 * drop student test
	 */
	@Test
	public void dropCourse()  throws Exception {
		
		MockHttpServletResponse response;
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .get("/student?email=test@csumb.edu")
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		// verify student exists
		StudentDTO[] dto_list = fromJsonString(response.getContentAsString(), StudentDTO[].class);
		boolean found = false;
		int id = 0;
		for (StudentDTO dto : dto_list) {
			if (dto.email().contains("test@csumb.edu") ) {
				found=true;
				id = dto.id();
			}
		}
		assertTrue(found);

		// drop student test
		response = mvc.perform(
				MockMvcRequestBuilders
			      .delete("/students/"+id))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
	
		// verify student doesn't exist anymore
		response = mvc.perform(
				MockMvcRequestBuilders
			      .get("/student?email=test@csumb.edu")
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		dto_list = fromJsonString(response.getContentAsString(), StudentDTO[].class);
		found = false;
		int size = 0;
		for (StudentDTO dto : dto_list) {
			size++;
			if (dto.email().contains(null)) found=true;
		}
		if(size == 0) {
			assertTrue(true);
		}
		assertFalse(found);
	}
	
	
	
	
	
	

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
