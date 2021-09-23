package gradeCalc.model;

public interface CourseRegisterObserver {
	
	public void registerUpdated();
	
	public void showMessage(String message, String messageType);

}
