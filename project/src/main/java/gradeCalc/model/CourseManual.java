package gradeCalc.model;

import java.io.Serializable;

public class CourseManual implements Icourse, Serializable {
	

	private static final long serialVersionUID = -2557080959567734500L;
	private String courseID;
	private String courseName;
	private String courseResult;
	private double sp;
	private double average;
	
	public CourseManual(String CourseID, String CourseName, String CourseResult, double SP) {
		if (!CourseID.isEmpty())
			this.courseID = CourseID;
		if (!CourseName.isEmpty())
			this.courseName = CourseName;
		
		if (!CourseResult.isEmpty())
			this.courseResult = CourseResult;
		
		if (SP >= 0)
			this.sp = SP;
		else
			this.sp = 0;
		
		this.average = -1;
		
	}
	
	public CourseManual(String CourseID, String CourseName, String CourseResult, double SP, double average) {
		
		if (!CourseID.isEmpty())
			this.courseID = CourseID;
		if (!CourseName.isEmpty())
			this.courseName = CourseName;
		
		if (!CourseResult.isEmpty())
			this.courseResult = CourseResult;
		
		if (SP >= 0)
			this.sp = SP;
		else
			this.sp = 0;
		
		this.average = average;
	}

	@Override
	public int getCourseResult() {
		return HelperFunctions.stringToInt(courseResult);
	}
	
	@Override
	public String getCourseResultString() {
		return this.courseResult;
	}
	
	@Override
	public void setCourseResult(String courseResult) {
		this.courseResult = courseResult;
	}

	@Override
	public String getCourseID() {
		return courseID;
	}

	@Override
	public String getCourseName() {
		return courseName;
	}

	@Override
	public double getSP() {
		return sp;
	}
	
	@Override
	public String getOverUnderAverage() {
		if (this.getCourseResultString().equals("Godkjent")) {
			return "N/A";
		}
		else if (this.average == -1) {
			return "N/A";
		}
		else {
			double overUnder = HelperFunctions.round(HelperFunctions.stringToInt(this.courseResult) - this.average, 2);
			return String.valueOf(overUnder);
		}
	}
	
	
	@Override
	public String toString() {
		if (this.average == -1)
			return this.courseID + ";" + this.courseName + ";" + this.sp + ";" + String.valueOf(this.courseResult) + ";-1";
		else
			return this.courseID + ";" + this.courseName + ";" + this.sp + ";" + String.valueOf(this.courseResult) + ";" + this.average;
	}
	
	
	
	
	

}
