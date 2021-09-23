package gradeCalc.fxui;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * A simple logger
 * @author mathigb
 *
 */
public class Log extends FolderHandler implements Ilogger {

	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	private static final String EXTENTION = "txt";
	private static final String PARENT_FOLDER = "Log";
	private static final String NAME = "log";
	
	public Log() {
		super(PARENT_FOLDER, EXTENTION);
	}
	
	public File getFile() {
		var logPath = getPath(NAME);
//		ensureUserFolder();
		return logPath.toFile();
	}

	@Override
	public void write(String s) {
		StringBuilder sb = new StringBuilder();
		var logPath = getPath(NAME);
		ensureUserFolder();
		
		try {
			Scanner scanner = new Scanner(logPath.toFile());
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine() + "\n");
			}
			scanner.close();
		}
		catch (Exception e) {
			
		}
		
		try {
			sb.append("Timestamp: " + LocalTime.now().format(formatter) + ". " + s);
			PrintWriter writer = new PrintWriter(logPath.toFile());
			writer.print("");
			writer.print(sb.toString());
			writer.close();
			writer.flush();
		}
		catch (Exception e) {
			
		}
		
	}

	@Override
	public void delete() {
		super.deleteFile(NAME);
	}


}
