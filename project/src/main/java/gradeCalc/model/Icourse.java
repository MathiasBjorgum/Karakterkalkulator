package gradeCalc.model;

/**
 * An interface to implement different types of course input
 * 
 * @author mathigb
 *
 */
public interface Icourse {
	
	/**
	 * Fetches the courseID from the classes that implements this interface
	 * @return The CourseID.
	 */
	String getCourseID();
	
	int getCourseResult();
	
	String getCourseName();
	
	double getSP();
	
	/**
	 * @param s representation of grade.
	 * @return None.
	 */
	void setCourseResult(String s);
	
	/**
	 * 
	 * @return A string representation of the data.
	 */
	String toString();
	
	String getCourseResultString();
	
	String getOverUnderAverage();
	

}
