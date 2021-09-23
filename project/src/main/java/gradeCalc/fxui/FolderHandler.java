package gradeCalc.fxui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * A class for creating folders for other classes that extends this superclass.
 * It also provides a logger for its subclasses.
 * 
 * 
 * @author mathigb
 *
 */

public class FolderHandler {
	
	private final String EXTENTION;
	private final String PARENT_FOLDER;
	
	/**
	 * Provides a logger for subclasses of {@link FolderHandler}.
	 */
	protected static Ilogger logger = new Log();
	
	/**
	 * Creates folders in the "user.home/GradeCalculator" directory given an file extension and parent folder
	 * 
	 * @param parentFolder The parent folder of the file
	 * @param extention The file extension
	 */
	
	protected FolderHandler(String parentFolder, String extention) {
		EXTENTION = extention;
		PARENT_FOLDER = parentFolder;
	}
	
	protected Path getFolderPath() {
		return Path.of(System.getProperty("user.home"), "GradeCalculator", PARENT_FOLDER);
	}
	
	protected boolean ensureUserFolder() {
        try {
        	Files.createDirectories(getFolderPath());
            return true;
        }
        catch (IOException ioe) {
        	return false;
        }
    }
	
	protected Path getPath(String name) {
        return getFolderPath().resolve(name + "." + EXTENTION);
    }
	
	protected void deleteFile(String name) {
		logger.write("Sletter fil: " + getPath(name).toString());
		getPath(name).toFile().delete();
	}
	
	private static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	public static void deleteDirectoryDriver() {
		deleteDirectory(Path.of(System.getProperty("user.home"), "GradeCalculator").toFile());
	}
	



}
