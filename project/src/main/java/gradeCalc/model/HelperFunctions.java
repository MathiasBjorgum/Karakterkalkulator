package gradeCalc.model;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

import gradeCalc.fxui.Ilogger;
import gradeCalc.fxui.Log;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class HelperFunctions {
	
	private static Ilogger logger = new Log();
	
	public static int stringToInt(String s) {
		if (s.equals("Godkjent")) {
			return -1;
		}
		else if (s.equals("Ikke godkjent/F")) {
			return -2;
		}
		else {
			return 6 - (((int) s.toUpperCase().charAt(0)) - 64);	
		}
	}
	
	public static void showErrorBox(String message) {
		if (message.contains("/n"))
			logger.write(message.substring(0, message.indexOf("\n")));
		else
			logger.write(message);
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
//		
//		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showMessageBox(String message, String label) {
//		JOptionPane.showMessageDialog(null, message, label, JOptionPane.INFORMATION_MESSAGE);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle(label);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
		
	}
	
//	public static void showMessageBox(String label, JScrollPane scrollPane) {
//		JOptionPane.showMessageDialog(null, scrollPane, label, JOptionPane.INFORMATION_MESSAGE);
//	}
//	
	public static boolean showConfirmBox(String message) {
//		Object[] options = { "JA", "NEI" };
//		int result = JOptionPane.showOptionDialog(null, message, "Karakterkalkulator", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
//		
//		if (result == 0)
//			return true;
//		else
//			return false;
		
		Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.YES)
			return true;
		return false;
	}
	
	public static String fixedLengthString(String string, int length)  {
		return String.format("%-" + length + "." + length + "s", string);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String readStringFromFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			
			while (scanner.hasNextLine()) {
				
			sb.append(scanner.nextLine()+ "\n");

			}
		scanner.close();	
		return sb.toString();
		
		}
		
		catch (Exception e) {
			logger.write(e.toString());
			return "Kunne ikke hente tekst";
		}
		
	}
	
	public static void main(String[] args) {
		showMessageBox("test", "hh");
	}

}
