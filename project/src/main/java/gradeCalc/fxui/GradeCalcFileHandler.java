package gradeCalc.fxui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import gradeCalc.model.CourseManual;
import gradeCalc.model.CourseRegister;
import gradeCalc.model.Icourse;

public class GradeCalcFileHandler extends FolderHandler implements IgradeCalcFileHandler {
	
	private static final String EXTENTION = "reg";
	private static final String PARENT_FOLDER = "Register";

	public GradeCalcFileHandler() {
		super(PARENT_FOLDER, EXTENTION);
	}
	
	@Override
	public void deleteFile(String name) {
		super.deleteFile(name);
	}
	
	@Override
	public void saveRegister(CourseRegister register, String name) throws IOException {
		var registerPath = getPath(name);
		ensureUserFolder();
		try (var output = new FileOutputStream(registerPath.toFile())) {
            saveRegister(register, output);
            logger.write("Lagrer filen" + registerPath.toString());
        }
		
	}
	
	@Override
	public void saveRegister(CourseRegister register, OutputStream os) {
		try {
			PrintWriter writer = new PrintWriter(os);
			writer.println("Navn på registeret: " + register.getName());
			writer.println("Fagkode,Fagnavn,Studiepoeng,Karakter,Gjennomsnittlig karakter");
			for (Icourse course : register) {
				writer.println(course);

			}
			writer.flush();
			writer.close();
			logger.write("Registeret i tekstform har blitt lagret til fil");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public CourseRegister readRegister(InputStream is) {
		CourseRegister register = null;
		try (var scanner = new Scanner(is)) {
			register = new CourseRegister();
			register.setName(scanner.nextLine().substring(20));
			scanner.nextLine();
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] lineInfo = line.split(";");
				
				String CourseID = lineInfo[0];
				String CourseName = lineInfo[1];
				String CourseResult = lineInfo[3];
				double SP = Double.parseDouble(lineInfo[2]);
				double average = Double.parseDouble(lineInfo[4]);
				
				register.addManualCourse(new CourseManual(CourseID, CourseName, CourseResult, SP, average));
			}
			scanner.close();
			logger.write("Registeret i tekstform har blitt lest fra fil");
			
			return register;
		}
		
	}
	
	public CourseRegister readRegister(String name) throws IOException {
		var registerPath = getPath(name);
		try (var input = new FileInputStream(registerPath.toFile())) {
			logger.write("Leser fil: " + registerPath.toString());
			return readRegister(input);
		}
	}

}
