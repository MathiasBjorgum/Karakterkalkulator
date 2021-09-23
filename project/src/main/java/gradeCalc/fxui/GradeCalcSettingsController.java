package gradeCalc.fxui;

import gradeCalc.model.HelperFunctions;

//import java.awt.Dimension;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GradeCalcSettingsController {
	
	private GradeCalcController gradeCalcController;
	
	private static Ilogger logger = new Log();
	
	private IgradeCalcSettingsFileHandler settingsFilehandler = new GradeCalcSettingsFileHandler();;
	
	void setGradeCalcController(final GradeCalcController controller) {
		this.gradeCalcController = controller;
	}
	
	private GradeCalcSettings settings = new GradeCalcSettings();
	
	public void setSettings(final GradeCalcSettings settings) {
		this.settings = settings;
	}
	
	@FXML Text helpText;
	
	@FXML Button backButton, clearRegister, importButton, saveButton, readLogButton, resetSettings, deleteAll;
	
	@FXML ChoiceBox<String> choiceBoxSort, choiceBoxSort1, choiceBoxFileHandler;
	
	@FXML TextField registerNameTxtField;
	
	
	@FXML
	private void initialize() {
		setHelpText();
		
		choiceBoxSort.getItems().setAll("Fagkode", "Fagnavn" ,"Resultat", "Beste i forhold til andre");
		choiceBoxFileHandler.getItems().setAll("Tekstbasert","Serializable");
		choiceBoxSort1.getItems().setAll("Høyeste først", "Laveste først");
		
		registerNameTxtField.setTooltip(new Tooltip("Skriv inn et navn for å kunne lagre flere registere. Registeret kan da lastes ved å skrive inn samme navn igjen."));
		resetSettings.setTooltip(new Tooltip("Tilbakestillger innstillingene til standard"));
		deleteAll.setTooltip(new Tooltip("Sletter alle brukerlagde filer knyttet til applikasjonen"));
		clearRegister.setTooltip(new Tooltip("Sletter registeret, inkludert en eventuell lagret fil med samme navn"));
		readLogButton.setTooltip(new Tooltip("Viser loggen"));
		
		registerNameTxtField.setText("Default");
		
		// Importerer settings fra fil
		try {
			GradeCalcSettings oldSettings = settingsFilehandler.readSettings();
			oldSettings.copyInto(this.settings);
		}
		catch (Exception e) {
			logger.write("Finner ingen settings å laste fra. Bruker standardinnstillinger");
		}
		
		updateView();

	}
	
	@FXML
	void updateView() {
		choiceBoxSort.getSelectionModel().select(settings.getSortMethod().ordinal());
		choiceBoxFileHandler.getSelectionModel().select(settings.getFileHandling().ordinal());
		choiceBoxSort1.getSelectionModel().select(settings.getSortOrder().ordinal());
		
		if (gradeCalcController != null)
			registerNameTxtField.setText(gradeCalcController.getRegisterName());
	}
	
	@FXML
	void save() {
		applySettings();
		gradeCalcController.saveToFile();
	}
	
	@FXML
	void load() {
		applySettings();
		gradeCalcController.importFromFile(this.registerNameTxtField.getText(), true);
	}
	
	@FXML
	private void handleBackButton() {
		
		applySettings();
		gradeCalcController.goBack();
	}
	
	@FXML
	private void clearRegister() {
		gradeCalcController.clearContents(this.registerNameTxtField.getText());
	}
	
	@FXML
	private void deleteAll(ActionEvent event) {
		if (HelperFunctions.showConfirmBox("Er du sikker på at du vil slette alle filene tilknyttet denne applikasjonen?"
				+ "\nDette sletter ALT og lukker applikasjonen")) {
			FolderHandler.deleteDirectoryDriver();
			
			Stage stage = (Stage) deleteAll.getScene().getWindow();
		    stage.close();
		}
	}
	
	
	@FXML
	private void resetSettings() { 
		this.settings = new GradeCalcSettings();
		logger.write("Tilbakestiller instillinger");
		updateView();
		applySettings();
	}
	
	@FXML
	private void readLog() {
//		JTextArea textArea = new JTextArea(HelperFunctions.readStringFromFile(new Log().getFile()));
//		JScrollPane scrollPane = new JScrollPane(textArea);  
//		textArea.setLineWrap(true);  
//		textArea.setWrapStyleWord(true); 
//		scrollPane.setPreferredSize( new Dimension(600, 200 ) );
		HelperFunctions.showMessageBox(HelperFunctions.readStringFromFile(new Log().getFile()), "log");
//		HelperFunctions.showMessageBox("log", scrollPane);
	}
	
	@FXML
	private void applySettings() {
		this.settings.setFileHandling(GradeCalcSettings.FileHandling.values()[choiceBoxFileHandler.getSelectionModel().getSelectedIndex()]);
		this.settings.setSortMethod(GradeCalcSettings.SortMethod.values()[choiceBoxSort.getSelectionModel().getSelectedIndex()]);
		this.settings.setSortOrder(GradeCalcSettings.SortOrder.values()[choiceBoxSort1.getSelectionModel().getSelectedIndex()]);
		gradeCalcController.setRegisterName(this.registerNameTxtField.getText());
		gradeCalcController.applySettings(this.settings);
	}
	
	private void setHelpText() {
		String helpText = "Dette er en applikasjon som beregner karaktersnitt. Dersom du er student ved NTNU er inntastingen veldig enkelt! Det eneste du trenger å gjøre er å taste inn fagkode og karakter så henter applikasjonen ut data fra grades.no sitt API."
				+ "\n\nFor å fjerne et fag fra listen skriver du inn fagkoden i \"Fagkode\"-feltet. Dersom fagkoden finnes i registeret vil \"registrer\"-knappen endre seg til \"fjern\"."
				+ "\n\nAlle filer generert av dnne applikasjonen blir lagret i user.home/GradeCalculator."
				+ "\n\nI applikasjonen er det to forskjellige metoder å lagre filer på."
				+ "\n\nDet ene er tekstbasert, som innebærer at informasjonen blir lagret som en fil av typen NAME.reg. Denne filen er en tekstlig representasjon av registeret."
				+ "\n\nDet andre er ved hjelp av java sitt Serializable interface. Da blir objektene i sin helhet lagret til fil i NAME.regs. Denne filen er imidlertid ikke så lett å lese manuelt."
				+ "\n\nAll lesing til og fra fil, samt feilmeldinger, er lagret i log.txt under 'GradeCalculator/Log/log.txt'. Den kan også vises ved å trykke på knappen 'Les log'."
				+ "";
		this.helpText.setText(helpText);
	}
	

}
