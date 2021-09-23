package gradeCalc.model;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import gradeCalc.fxui.GradeCalcSettings;
import gradeCalc.fxui.Ilogger;
import gradeCalc.fxui.Log;

/**
 * A register for classes that implement the {@link Icourse} interface. This class have methods for manipulating the register in several ways.
 * 
 * @author mathigb
 *
 */
public class CourseRegister implements Serializable, Iterable<Icourse>{
	
	private static final long serialVersionUID = 1L;
	
	private double gradeAverage;
	
	private double courseCredits;
	
	private double totalOverUnderAverage;
	
	private Collection<CourseRegisterObserver> observers = new ArrayList<>();
	
	private List<Icourse> courses = new ArrayList<>();
//	private List<AbstractCourse> courses = new ArrayList<>();
	
	private Ilogger logger = new Log();
	
	// Sets a default name
	private String name = "Default";
	
	public CourseRegister() {
		
	}
	
	public CourseRegister(String name) {
		this.name = name;
	}
	
		/**
	 * A method to get all courseIDS
	 * 
	 * @return List of all CourseID to uppercase.
	 */
	public List<String> getCourseIDS() {
		List<String> courseids = new ArrayList<>();
		
		for (Icourse course : this) {
			courseids.add(course.getCourseID().toUpperCase());
		}
		return courseids;
	}
	
	
	/**
	 * Sorts the list based on it's {@link Icourse}.CourseID value
	 * 
	 * @return A sorted list of courses
	 */
	public List<Icourse> getCourses() {
		courses.sort((Icourse c1, Icourse c2) -> (c1.getCourseID().compareTo(c2.getCourseID())));
		return List.copyOf(this.courses);
	}

	/**
	 * Returns a sorted list based on an optional {@link GradeCalcSettings} instance.
	 * 
	 * @param settings {@link GradeCalcSettings} to decide sort order
	 * @return The sorted list
	 */
	public List<Icourse> getCourses(GradeCalcSettings settings) {
		sortCourses(this.courses, settings);
		return List.copyOf(this.courses);
	}
	
	private void sortCourses(List<Icourse> list, GradeCalcSettings settings) {
		
		switch(settings.getSortOrder()) {
		
		case HIGH_FIRST:
			switch(settings.getSortMethod()) {
			case COURSE_ID:
				list.sort((Icourse c1, Icourse c2) -> (c1.getCourseID().compareTo(c2.getCourseID())));
				break;
			case COURSE_NAME:
				list.sort((Icourse c1, Icourse c2) -> (c1.getCourseName().compareTo(c2.getCourseName())));
				break;
			case COURSE_RESULT:
				list.sort((Icourse c1, Icourse c2) -> (c1.getCourseResultString().compareTo(c2.getCourseResultString())));
				break;
			case OVER_UNDER_AVERAGE:
				list.sort((Icourse c1, Icourse c2) -> (c2.getOverUnderAverage().compareTo(c1.getOverUnderAverage())));
				break;
			}
			break;
			
		case LOW_FIRST:
			switch(settings.getSortMethod()) {
			case COURSE_ID:
				list.sort((Icourse c1, Icourse c2) -> (c2.getCourseID().compareTo(c1.getCourseID())));
				break;
			case COURSE_NAME:
				list.sort((Icourse c1, Icourse c2) -> (c2.getCourseName().compareTo(c1.getCourseName())));
				break;
			case COURSE_RESULT:
				list.sort((Icourse c1, Icourse c2) -> (c2.getCourseResultString().compareTo(c1.getCourseResultString())));
				break;
			case OVER_UNDER_AVERAGE:
				list.sort((Icourse c1, Icourse c2) -> (c1.getOverUnderAverage().compareTo(c2.getOverUnderAverage())));
				break;
			}
			break;
	}
	}

	public void addNTNUcourse(String courseID, String courseResult) {
		String reasonOfException = "";
		try {
			Icourse course = new CourseNTNU(courseID.toUpperCase(), courseResult);
			this.courses.add(course);
			logger.write("La til fag: " + course.getCourseID());
			fireRegisterUpdated();
			return;
		}
		catch (UnknownHostException ue) {
			reasonOfException = "Du har ikke internett.";
		}
		catch (FileNotFoundException fe) {
			reasonOfException = "Faget finnes ikke i grades.no sin database";
		}
		catch (TimeoutException te) {
			reasonOfException = "Det tar for lang tid å nå grades.no sin database.";
		}
		catch (Exception e) {
			reasonOfException = e.toString();
		}
		
		logger.write("Registeret klarte ikke legge til faget: " + reasonOfException);
		fireRegisterMessage("Kunne ikke finne fag (vurder å bytt til manuell inntasting): " + courseID.toUpperCase() + ".\nÅrsak: " + reasonOfException, "error");
	}
	
	public void addManualCourse(CourseManual course) {

		if (validateManualCourse(course)) {
			this.courses.add(course);
			logger.write("La til fag: " + course.getCourseID());
			fireRegisterUpdated();
		}
	}

	
	private boolean validateManualCourse(CourseManual course) {
		
		if (course.getCourseResultString() == null) {
			fireRegisterMessage("Du har glemt å velge karakter.", "error");
			return false;
		}
		else if (course.getCourseID() == null) {
			fireRegisterMessage("Fagkode kan ikke være blank.", "error");
			return false;
		}
		
		else if (course.getCourseName() == "" || course.getCourseName() == null) {
			fireRegisterMessage("Fagnavn kan ikke være blank.", "error");
			return false;
		}
		else if (course.getSP() == 0) {
			fireRegisterMessage("Antall studiepoeng er ikke riktig.", "error");
			return false;
		}
		
		
		else if (this.getCourseIDS().contains(course.getCourseID().toUpperCase())) {
			this.removeCourse(course.getCourseID());
			logger.write("Fjernet fag: " + course.getCourseID());
			return false;
		}

		
		return true;
	}
	
	private void fireRegisterMessage(String message, String messageType) {
		for (CourseRegisterObserver observer : observers) {
			observer.showMessage(message, messageType);
		}
	}
	
	private void fireRegisterUpdated() {
		for (CourseRegisterObserver observer : observers) {
			observer.registerUpdated();
		}
	}
	
	public void addRegisterObserver(CourseRegisterObserver observer) {
		this.observers.add(observer);
	}
	
	
	public void updateAverage() {
		double sumOfGrades = 0;
		double courseCredit = 0;
		
		double sumOverUnder = 0;
		double numberOverUnder = 0;

		for (Icourse currentCourse : this)
		{
//			Håndterer tilfellet ved "Godkjent"
			if (currentCourse.getCourseResult() == -1) {
			}
//			Hånterer tilfellet "Ikke godkjent"
			else if (currentCourse.getCourseResult() == -2) {
				courseCredit += currentCourse.getSP();
			}
			else {
				courseCredit += currentCourse.getSP();
				sumOfGrades += (currentCourse.getCourseResult() * currentCourse.getSP());
				
				if (!currentCourse.getOverUnderAverage().equals("N/A")) {
					sumOverUnder += Double.parseDouble(currentCourse.getOverUnderAverage());
					numberOverUnder ++;
				}
			}
		}
		this.gradeAverage = sumOfGrades / courseCredit;
		this.totalOverUnderAverage = sumOverUnder / numberOverUnder;
	}
	
	public double getAverage() {
		return this.gradeAverage;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getCourseCredit() {
		updateCourseCredit();
		return this.courseCredits;
	}
	
	private void updateCourseCredit() {
		double tmpCourseCredit = 0;
		
		for (Icourse course : this) {
			tmpCourseCredit += course.getSP();
		}
		
		this.courseCredits = tmpCourseCredit;
	}
	
	public void removeCourse(String CourseID) {

		Icourse removing = null;
		for (Icourse course : this) {
			if (course.getCourseID().toUpperCase().equals(CourseID.toUpperCase()))
				removing = course;
		}

		courses.remove(removing);
		updateAverage();
	}
	
	public double getOverUnderAverage() {
		updateAverage();
		return this.totalOverUnderAverage;
	}
	
	@Override
	public Iterator<Icourse> iterator() {
		courses.sort((Icourse c1, Icourse c2) -> (c1.getCourseID().compareTo(c2.getCourseID())));
		return courses.iterator();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<Icourse> iterator = courses.iterator();
		while (iterator.hasNext()) {
			Icourse currentCourse = iterator.next();
			sb.append(currentCourse.toString() + "\n");
		}
		
		return (sb.toString());
	}
	
	public static void main(String[] args) {
		CourseRegister register = new CourseRegister();
		
		register.addNTNUcourse("met1001", "B");
		
//		System.out.println(register.toString());
	}

	
}







