package com.cst438;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@SpringBootTest
public class EndToEndStudent {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/661be/438/chromedriver-win64/chromedriver-win64/chromedriver.exe";
	
	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "bbuilder@csumb.edu";

	public static final String TEST_STUDENT_NAME = "Bob Builder"; 

	public static final int SLEEP_DURATION = 1000; // 1 second.


	/*
	 * add student 
	 */
	
	@Test
	public void addStudentTest() throws Exception {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// select the admin tab
			driver.findElement(By.id("AddStudent")).click();
			Thread.sleep(SLEEP_DURATION);

			// select the AddStudent button
			driver.findElement(By.id("NewStudent")).click();
			
			// enter student information
			
			driver.findElement(By.name("name")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.name("email")).sendKeys(TEST_USER_EMAIL);

			Thread.sleep(SLEEP_DURATION);
			
			// select Add Student button
			driver.findElement(By.id("Add")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// close add student modal
			driver.findElement(By.id("Close")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify new student in students list	
			WebElement we = driver.findElement(By.xpath("//tr[td='"+TEST_STUDENT_NAME+"']"));
			assertNotNull(we, "Test student name not found in student list after successfully adding the student.");		

		} catch (Exception ex) {
			throw ex;
		} finally {
			driver.quit();
		}
	}
	

	/*
	 * delete a student 
	 */
	
	@Test
	public void deleteStudentTest() throws Exception {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// select the admin tab
			driver.findElement(By.id("AddStudent")).click();
			Thread.sleep(SLEEP_DURATION);

			// select the AddStudent button
			driver.findElement(By.id("NewStudent")).click();
			
			// enter student information
			driver.findElement(By.name("name")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.name("email")).sendKeys(TEST_USER_EMAIL);

			Thread.sleep(SLEEP_DURATION);
			
			// select Add Student button
			driver.findElement(By.id("Add")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// close add student modal
			driver.findElement(By.id("Close")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify new student in students list	
			WebElement we = driver.findElement(By.xpath("//tr[td='"+TEST_STUDENT_NAME+"']"));
			assertNotNull(we, "Test student name not found in student list after successfully deleting the student.");
			
			// drop student
			WebElement dropButton = we.findElement(By.id("deleteStudent"));
			dropButton.click();
						
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			driver.quit();
		}
		
	}

	/*
	 * edit a student 
	 */
	
	@Test
	public void editStudentTest() throws Exception {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// select the admin tab
			driver.findElement(By.id("AddStudent")).click();
			Thread.sleep(SLEEP_DURATION);

			// select the AddStudent button
			driver.findElement(By.id("NewStudent")).click();
			
			// enter student information
			driver.findElement(By.name("name")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.name("email")).sendKeys(TEST_USER_EMAIL);

			Thread.sleep(SLEEP_DURATION);
			
			// select Add Student button
			driver.findElement(By.id("Add")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// close add student modal
			driver.findElement(By.id("Close")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify new student in students list	
			WebElement we = driver.findElement(By.xpath("//tr[td='"+TEST_STUDENT_NAME+"']"));
			assertNotNull(we, "Test student name not found in student list after successfully deleting the student.");
			
			// drop student
			WebElement editButton = we.findElement(By.id("editStudent"));
			editButton.click();
			
			// replace student information
			driver.findElement(By.name("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "Tom Cat");
			driver.findElement(By.name("email")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "tcat@csumb.edu");

			// select Add Student button
			driver.findElement(By.id("editSave")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// close add student modal
			driver.findElement(By.id("Close")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// verify edits name
			we = driver.findElement(By.xpath("//tr[td='Tom Cat']"));
			assertNotNull(we, "Test student name not found in student list after successfully deleting the student.");
							
			// verify edits email
			we = driver.findElement(By.xpath("//tr[td='tcat@csumb.edu']"));
			assertNotNull(we, "Test student email not found in student list after successfully deleting the student.");
						
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			driver.quit();
		}
	}
	
}