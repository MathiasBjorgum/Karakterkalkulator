package gradeCalc.model;

public abstract class AbstractCourse {
	
	private String courseID;
	private String courseName;
	private String courseResult;
	private double sp;
	private double average;
	
	public AbstractCourse(String courseID, String courseName, double sp, double average) {
		if (!courseID.isEmpty())
			this.courseID = courseID;
		if (!courseName.isEmpty())
			this.courseName = courseName;
		
		if (sp >= 0)
			this.sp = sp;
		else
			this.sp = 0;
		
		this.average = average;
	}
	
	public AbstractCourse(String courseID) {
		if (!courseID.isEmpty())
			this.courseID = courseID;
	}
	
	public int getCourseResult() {
		return HelperFunctions.stringToInt(courseResult);
	}
	
	public String getCourseResultString() {
		return this.courseResult;
	}
	
	public void setCourseResult(String s) {
		this.courseResult = s;
	}
	
	
	
	
	
	
	
	public String toString() {
		return this.courseID + ";" + this.courseName + ";" + this.sp + ";" + String.valueOf(this.courseResult) + ";" + this.average;
	}
	
	public static void main(String[] args) {
		
	}

}
