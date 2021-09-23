package gradeCalc.fxui;

import java.io.Serializable;

/**
 * 
 * Sets settings for the {@link GradeCalcController} to use. The settings are specified in the listed enums.
 * 
 * @author mathigb
 *
 */

public class GradeCalcSettings implements Serializable {
	
	private static final long serialVersionUID = 3068932024120428790L;

	
	/**
	 * 
	 * @author mathi
	 *
	 */
	public enum SortMethod {
		COURSE_ID, COURSE_NAME, COURSE_RESULT, OVER_UNDER_AVERAGE;
	}
	
	public enum FileHandling {
		TXT_BASED, SERIALIZABLE;
	}
	
	public enum SortOrder {
		HIGH_FIRST, LOW_FIRST;
	}
	
	private SortMethod sortMethod = SortMethod.COURSE_ID;
	private FileHandling fileHandling = FileHandling.TXT_BASED;
	private SortOrder sortOrder = SortOrder.HIGH_FIRST;
	
	public GradeCalcSettings() {
		
	}
	
	public void copyInto(final GradeCalcSettings target) {
		target.setSortMethod(this.getSortMethod());
		target.setFileHandling(this.getFileHandling());
		target.setSortOrder(this.getSortOrder());
	}
	
	public SortMethod getSortMethod() {
		return this.sortMethod;
	}
	
	public FileHandling getFileHandling() {
		return this.fileHandling;
	}
	
	public SortOrder getSortOrder() {
		return this.sortOrder;
	}
	
	public void setSortMethod(final SortMethod sortMethod) {
		this.sortMethod = sortMethod;
	}
	
	/**
	 * Sets filehandling method of the program
	 * 
	 * @param fileHandling
	 */
	public void setFileHandling(final FileHandling fileHandling) {
		this.fileHandling = fileHandling;
	}
	
	public void setSortOrder(final SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
	@Override
	public String toString() {
		return "FileHandling: " + this.fileHandling.toString() + 
				"\nSortMethod: " + this.sortMethod.toString() +
				"\nSortOrder: " +this.sortOrder.toString()
				;
	}

}
