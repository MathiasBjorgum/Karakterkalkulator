package gradeCalc.fxui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface IgradeCalcSettingsFileHandler {
	
	public void saveSettings(GradeCalcSettings settings) throws IOException;
	
	public void saveSettings(GradeCalcSettings settings, OutputStream os);
	
	public GradeCalcSettings readSettings() throws IOException;
	
	public GradeCalcSettings readSettings(InputStream is);
	
	public void deleteFile(String name);
	

}
