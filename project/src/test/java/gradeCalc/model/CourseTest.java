package gradeCalc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import gradeCalc.model.CourseManual;
import gradeCalc.model.CourseNTNU;
import gradeCalc.model.Icourse;

public class CourseTest {
	
	
	Icourse c1;
	Icourse c2;
	CourseNTNU c3;
	Icourse c4;
	Icourse c5;

	
	@Test
	public void testNTNU() {
		try {
			c1 = new CourseNTNU("MET1001");
		} catch (Exception e) {
			fail("Når ikke grades sin database");
		}
		
		assertThrows(FileNotFoundException.class, () -> {
			c3 = new CourseNTNU("MET5");
		});
		
		
		c1.setCourseResult("B");
		
		assertEquals("Matematikk for økonomer", c1.getCourseName());
		assertEquals(4, c1.getCourseResult());
		assertEquals(7.5, c1.getSP());
		
		assertFalse(c3.validateCourse());
		
		c1.setCourseResult("Godkjent");
		
		assertEquals("N/A", c1.getOverUnderAverage(), "fag med 'Godkjent' skal returnere 'N/A'");
	}
	
	
	@Test
	public void testManual() {
		
		c2 = new CourseManual("MET101", "Matte", "C", 10, 4.1568);

		c4 = new CourseManual("BØK540", "BedØk", "A", -10);
		c5 = new CourseManual("TST101", "Testing 101", "Godkjent", 7.5);
		
		assertEquals("Matte", c2.getCourseName());
		assertEquals("MET101", c2.getCourseID());
		assertEquals(3, c2.getCourseResult());
		
		assertEquals("-1.16", c2.getOverUnderAverage());
		
		assertEquals(0 ,c4.getSP());
		
		assertEquals("N/A", c5.getOverUnderAverage(), "fag med 'Godkjent' skal returnere 'N/A'");
		
	}
	

	
	
	
	
	
	
	

}
