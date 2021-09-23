package gradeCalc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gradeCalc.fxui.GradeCalcSettings;
import gradeCalc.fxui.GradeCalcSettings.SortOrder;
import gradeCalc.model.CourseManual;
import gradeCalc.model.CourseRegister;
import gradeCalc.model.Icourse;

public class CourseRegisterTest {
	
	
	CourseRegister register = new CourseRegister();
	
	
	@BeforeEach
	public void setup() {
		
		register.addManualCourse(new CourseManual("MET1001", "Matematikk for økonomer", "B", 7.5));
		register.addManualCourse(new CourseManual("BØA3020", "Integrert budsjettering og økonomisk styring", "A", 7.5));
		register.addManualCourse(new CourseManual("TDT4100", "Objektorientert programmering", "A", 7.5));
		
		register.addManualCourse(new CourseManual("MET101", "Matte", "C", 7.5));
		
		register.updateAverage();
		
	}
	
	@Test
	public void testAverage() {
		assertEquals(4.25, register.getAverage());
		
		register.addManualCourse(new CourseManual("BØ205", "Bedriftsøkonomi", "D", 15.0));
		register.updateAverage();
		
		assertEquals(3.5, register.getAverage(), "15 studiepoeng skal telle mer enn 7.5");
		
		register.addManualCourse(new CourseManual("TDT4110","ITGK", "Godkjent", 7.5));
		
		assertEquals(3.5, register.getAverage(), "'Godkjent' skal ikke telle på snittet");
	}
	
	@Test
	public void testDefaultSort() {
		assertEquals("BØA3020", register.getCourses().get(0).getCourseID());
		assertEquals("MET1001", register.getCourses().get(1).getCourseID());
		assertEquals("MET101", register.getCourses().get(2).getCourseID());
		assertEquals("TDT4100", register.getCourses().get(3).getCourseID());
	}
	
	@Test
	public void testCustomSort() {
		GradeCalcSettings settings = new GradeCalcSettings();
		settings.setSortOrder(SortOrder.LOW_FIRST);
		
		List<Icourse> courses = register.getCourses(settings);
		
		assertEquals("TDT4100", courses.get(0).getCourseID());
		assertEquals("MET101", courses.get(1).getCourseID());
		assertEquals("MET1001", courses.get(2).getCourseID());
		assertEquals("BØA3020", courses.get(3).getCourseID());
	}
	
	@Test
	public void testRemoveCourse() {
		register.removeCourse("TDT4100");
		assertEquals(3, register.getCourses().size());
	}
	
	

}
