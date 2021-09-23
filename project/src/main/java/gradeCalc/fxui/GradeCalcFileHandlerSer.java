package gradeCalc.fxui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import gradeCalc.model.CourseRegister;

public class GradeCalcFileHandlerSer extends FolderHandler implements IgradeCalcFileHandler {

	
	private static final String EXTENTION = "regs";
	private static final String PARENT_FOLDER = "Register";
	
	public GradeCalcFileHandlerSer() {
		super(PARENT_FOLDER, EXTENTION);
	}

	public void deleteFile(String name) {
		super.deleteFile(name);
	}
	
	@Override
	public void saveRegister(CourseRegister register, String name) throws IOException {
		var registerPath = getPath(name);
		ensureUserFolder();
		try (var output = new FileOutputStream(registerPath.toFile())) {
            saveRegister(register, output);
            logger.write("Lagrer filen" + registerPath.toString());
        }

	}

	@Override
	public void saveRegister(CourseRegister register, OutputStream os) {
		try (FileOutputStream fos = (FileOutputStream) os;
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
					oos.writeObject(register);
			}
			catch (IOException e) {
				logger.write(e.toString());
			}

	}
	
	@Override
	public CourseRegister readRegister(InputStream is) {
		try {
			FileInputStream fis = (FileInputStream) is;
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Object obj = ois.readObject();
			ois.close();
			return (CourseRegister) obj;
		}
		catch (IOException | ClassNotFoundException e) {
			logger.write(e.toString());
			return new CourseRegister();
		}
	}

	@Override
	public CourseRegister readRegister(String name) throws IOException {
		var registerPath = getPath(name);
		try (var input = new FileInputStream(registerPath.toFile())) {
			logger.write("Leser filen: " + registerPath.toString());
			return readRegister(input);
		}
	}

	

}
