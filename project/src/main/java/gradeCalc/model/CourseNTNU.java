package gradeCalc.model;

import java.io.Serializable;

public class CourseNTNU implements Icourse, Serializable {
	

	private static final long serialVersionUID = 2054220215678023409L;
	private String courseID;
	private String courseName;
	private String courseResult;
	private double sp;
	private double average;
	
	
	public CourseNTNU(String CourseID) throws Exception {
		this.courseID = CourseID.toUpperCase();

			ImportFromAPI importer = new ImportFromAPI(CourseID);
			this.courseName = importer.getCourseName().toUpperCase();
			this.sp = importer.getCredit();
			this.average = importer.getAverage();

	}
	
	public CourseNTNU(String CourseID, String CourseResult) throws Exception {
		this.courseID = CourseID;

			ImportFromAPI importer = new ImportFromAPI(CourseID);
		this.courseName = importer.getCourseName();
		this.sp = importer.getCredit();
		this.average = importer.getAverage();
		setCourseResult(CourseResult);

	}
	
	@Override
	public String getCourseID() {
		return this.courseID;
	}
	
	@Override
	public String getCourseName() {
		return this.courseName;
	}

	@Override
	public int getCourseResult() {
		return  HelperFunctions.stringToInt(this.courseResult);
	}
	
	@Override
	public String getCourseResultString() {
		return this.courseResult;
	}
	
	@Override
	public double getSP() {
		return this.sp;
	}
	
	@Override
	public void setCourseResult(String s) {
		this.courseResult = s;
	}
	
	public boolean validateCourse() {
		if (this.getCourseName() == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public String getOverUnderAverage() {
		if (this.getCourseResultString().equals("Godkjent")) {
			return "N/A";
		}
		else if (this.average == 0.0) {
			return "N/A";
		}
		else {
			double overUnder = HelperFunctions.round(HelperFunctions.stringToInt(this.courseResult) - this.average, 2);
			return String.valueOf(overUnder);
		}
	}
	
	@Override
	public String toString() {
		return this.courseID + ";" + this.courseName + ";" + this.sp + ";" + String.valueOf(this.courseResult) + ";" + this.average;
	}

}
