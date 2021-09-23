package gradeCalc.model;

public class NTNU extends AbstractCourse{
	
	
	public NTNU(String courseID) throws Exception {
		super(courseID);
		ImportFromAPI importer = new ImportFromAPI(courseID);
//		this();
	}
	

	public NTNU(String courseID, String courseName, double sp, double average) {
		super(courseID, courseName, sp, average);
		// TODO Auto-generated constructor stub
	}
	
	
	

}
