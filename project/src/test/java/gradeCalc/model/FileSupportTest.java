package gradeCalc.model;

import org.junit.jupiter.api.Test;

import gradeCalc.fxui.GradeCalcFileHandler;
import gradeCalc.fxui.IgradeCalcFileHandler;
import gradeCalc.model.CourseManual;
import gradeCalc.model.CourseRegister;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class FileSupportTest {
	
	
	IgradeCalcFileHandler fs = new GradeCalcFileHandler();
	

	private CourseRegister getRegister() {
		CourseRegister register = new CourseRegister("testRegister");

		
		register.addManualCourse(new CourseManual("MET1001", "Matematikk for økonomer", "B", 7.5));
		register.addManualCourse(new CourseManual("BØA3020", "Integrert budsjettering og økonomisk styring", "A", 7.5));
		register.addManualCourse(new CourseManual("TDT4100", "Objektorientert programmering", "A", 7.5));
		
		register.addManualCourse(new CourseManual("MET101", "Matte", "C", 7.5));
		
		register.updateAverage();
		
		return register;
		
	}
	
	private String getStringRep() {
		String s = "Navn på registeret: testRegister"
				+ "\nFagkode,Fagnavn,Studiepoeng,Karakter,Gjennomsnittlig karakter"
				+ "\nBØA3020;Integrert budsjettering og økonomisk styring;7.5;A;-1"
				+ "\nMET1001;Matematikk for økonomer;7.5;B;-1 "
				+ "\nMET101;Matte;7.5;C;-1 "
				+ "\nTDT4100;Objektorientert programmering;7.5;A;-1";
		return s;
	}
	
	
	@Test
	public void testWriteToOS() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		CourseRegister register = getRegister();
		IgradeCalcFileHandler fh = new GradeCalcFileHandler();
		fh.saveRegister(register, os);
		
		
		String actual = new String(os.toByteArray()).replaceAll("\\s+","");
		String expected = getStringRep().replaceAll("\\s+","");
		
		assertEquals(expected, actual, "Written string representation is not correct.");
	}
	
	@Test
	public void testReadFromIS() throws UnsupportedEncodingException {
		InputStream is = new ByteArrayInputStream(getStringRep().getBytes("UTF-8"));
		
		IgradeCalcFileHandler fh = new GradeCalcFileHandler();
		CourseRegister actual = fh.readRegister(is);
		CourseRegister expected = getRegister();
		
		var actualIterator = actual.iterator();
		var expectedIterator = expected.iterator();
		
		int i = 0;
		while (expectedIterator.hasNext()) {
			try {
				assertEquals(expectedIterator.next().getCourseID(), actualIterator.next().getCourseID(), "Element mismatch at position " + i);
			} catch (IndexOutOfBoundsException e) {
				fail("The read register does not contain the correct number of elements.");
			}
			i++;
		}
		assertEquals(expected.getName(), actual.getName(), "Names does not match.");
	}
	

}
