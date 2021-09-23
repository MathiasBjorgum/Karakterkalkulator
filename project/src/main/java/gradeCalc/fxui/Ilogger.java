package gradeCalc.fxui;

import java.io.File;

/**
 * An interface to implement a simple logger. 
 * @author mathigb
 *
 */
public interface Ilogger {
	
	public void write(String s);
	
	public void delete();
	
	public File getFile();

}
