package gradeCalc.fxui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gradeCalc.model.CourseRegister;

public interface IgradeCalcFileHandler {
	
	/**
	 * Write a register to a file in a default location.
	 * @param todoList The register to write
	 * @throws IOException If a file at the proper location can't be written to
	 */
	public void saveRegister(CourseRegister register, String name) throws IOException;
	
	/**
	 * Write a register to a given OutputStream
	 * @param register The register to write
	 * @param os The stream to write to
	 */
	public void saveRegister(CourseRegister register, OutputStream os);
	
	/**
	 * Reads a register from it's default location
	 * 
	 * @return The register from it's default location
	 * @throws IOException If the register cant be read
	 */
	public CourseRegister readRegister(String name) throws IOException;
	
	/**
	 * Read a register from a given InputStream
	 * @param is The input stream to read from
	 * @return The register from the InputStream
	 */
	public CourseRegister readRegister(InputStream is);
	
	public void deleteFile(String name);
	
}
