package gradeCalc.fxui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class GradeCalcSettingsFileHandler extends FolderHandler implements IgradeCalcSettingsFileHandler {

	private static final String EXTENTION = "set";
	private static final String PARENT_FOLDER = "Settings";
	private static final String NAME = "settings";
	

	public GradeCalcSettingsFileHandler() {
		super(PARENT_FOLDER, EXTENTION);
	}
	
	public void deleteFile(String name) {
		super.deleteFile(name);
	}

	@Override
	public void saveSettings(GradeCalcSettings settings) throws IOException {
		var registerPath = getPath(NAME);
		ensureUserFolder();
		try (var output = new FileOutputStream(registerPath.toFile())) {
            saveSettings(settings, output);
            logger.write("Lagrer filen: " + registerPath.toString());
        }

	}

	@Override
	public void saveSettings(GradeCalcSettings settings, OutputStream os) {
		
		try (PrintWriter writer = new PrintWriter(os)) {
			writer.println(settings.toString());
		}

	}

	@Override
	public GradeCalcSettings readSettings() throws IOException {
		var registerPath = getPath(NAME);
		try (var input = new FileInputStream(registerPath.toFile())) {
			logger.write("Leser filen: " + registerPath.toString());
			return readSettings(input);
		}
	}

	@Override
	public GradeCalcSettings readSettings(InputStream is) {
		GradeCalcSettings settings = null;
		try (Scanner scanner = new Scanner(is)) {
			settings = new GradeCalcSettings();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String fileHandling = line.substring(line.indexOf(":")+2, line.length());
				line = scanner.nextLine();
				String sortMethod = line.substring(line.indexOf(":")+2, line.length());
				line = scanner.nextLine();
				String sortOrder = line.substring(line.indexOf(":")+2, line.length());
				
				settings.setFileHandling(GradeCalcSettings.FileHandling.valueOf(fileHandling));
				settings.setSortMethod(GradeCalcSettings.SortMethod.valueOf(sortMethod));
				settings.setSortOrder(GradeCalcSettings.SortOrder.valueOf(sortOrder));
			}
			
		}
		
		
		
		return settings;
	}

}
