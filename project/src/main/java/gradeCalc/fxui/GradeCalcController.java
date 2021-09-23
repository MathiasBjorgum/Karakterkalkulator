package gradeCalc.fxui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gradeCalc.model.CourseManual;
import gradeCalc.model.CourseRegister;
import gradeCalc.model.CourseRegisterObserver;
import gradeCalc.model.HelperFunctions;
import gradeCalc.model.Icourse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class GradeCalcController implements CourseRegisterObserver {
	
	private CourseRegister courseRegister;
	
	private IgradeCalcFileHandler filehandler = new GradeCalcFileHandler();
	
	private IgradeCalcSettingsFileHandler settingsFilehandler = new GradeCalcSettingsFileHandler();;
	
	private final GradeCalcSettings settings = new GradeCalcSettings();
	
	private boolean inputIsNTNU;
	
	private Ilogger logger = new Log();
	
	@FXML private AnchorPane anchorPane;
	
	@FXML private Text txtCourseCredit, average, txt1, courseCreditTxt, courseNameTxt, averageOverUnder, txtRegName;
	
	@FXML private Button registrer;
	
	@FXML private Button sceneSwitch;
	
	@FXML private TextField courseIDNTNU, courseName, courseCredit;

	@FXML private ImageView imageNTNU;
	
	@FXML private ChoiceBox<String> gradePicker;
	
	@FXML
	void initialize() {
		createGradePicker();
		gradePicker.getSelectionModel().select(0);
		
		courseRegister = new CourseRegister();
		
		courseRegister.addRegisterObserver(this);
		
		txt1.setText(setHeader());
		
		txtRegName.setText("Registernavn: " + this.courseRegister.getName());
		Tooltip.install(txtRegName, new Tooltip("Det er mulig å endre navnet på registeret under instillinger"));
		
		Tooltip.install(txtCourseCredit, new Tooltip("Totalt antall studiepoeng registrert i registeret"));
		
		average.setText("");
		averageOverUnder.setText("");
		registrer.setText("Registrer");
		
		this.sceneSwitch.setText("Bytt til manuell inntasting");
		this.inputIsNTNU = true;
		setCourseInput();
		
		
		// resetter logger
		logger.delete();
		
		// Registrerer fag når enter blir trykket på
		courseIDNTNU.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
		             handleRegister();
		        }
			}	
		});
		
		
	}
	
	private void createGradePicker() {
		List<String> myList = new ArrayList<>();
		myList.add("A");
		myList.add("B");
		myList.add("C");
		myList.add("D");
		myList.add("E");
		myList.add("Godkjent");
		myList.add("Ikke godkjent/F");
		
		ObservableList<String> availableChoices = FXCollections.observableList(myList); 
		gradePicker.setItems(availableChoices);
	}
	
	@FXML
	void handleRegister() {
		if (courseIDNTNU.getText().isBlank()) {
			HelperFunctions.showErrorBox("Fagkode kan ikke være blank");
		}
		else if (this.courseRegister.getCourseIDS().contains(courseIDNTNU.getText().toUpperCase())) {
			this.courseRegister.removeCourse(courseIDNTNU.getText().toUpperCase());
			registerUpdated();
		}
		else {
			logger.write("Prøver å legge til fag...");
			if (this.inputIsNTNU) {
				courseRegister.addNTNUcourse(this.courseIDNTNU.getText(), this.gradePicker.getValue());
				registerUpdated();
			}
			else {
				String ID = this.courseIDNTNU.getText().toUpperCase();
				String name = this.courseName.getText();
				String result = this.gradePicker.getValue();
				double credit;
				try {
					credit = Double.parseDouble(this.courseCredit.getText().replace(',', '.'));
				}
				catch (Exception e) {
					credit = 0;
				}
				
				courseRegister.addManualCourse(new CourseManual(ID, name, result, credit));
				registerUpdated();
			}
		}
	}
	
	@Override
	public void registerUpdated() {
		updateListView();
		try {
			updateAverage();
			}
		catch (Exception e) {}
		checkInput();
	}
	
	@Override
	public void showMessage(String message, String messageType) {
		if (messageType.equals("error"))
			HelperFunctions.showErrorBox(message);
	}

	@FXML
	private void switchInputMethod() {
		if (this.inputIsNTNU) {
			this.inputIsNTNU = false;
			setCourseInput();
			this.sceneSwitch.setText("Bytt til NTNU innntasting");
			this.imageNTNU.setVisible(false);
		}
		else {
			this.inputIsNTNU = true;
			setCourseInput();
			this.sceneSwitch.setText("Bytt til manuell inntasting");
			this.imageNTNU.setVisible(true);
		}
	}
	
	@FXML
	private void checkInput() {
		if (courseRegister.getCourseIDS().contains(courseIDNTNU.getText().toUpperCase())) {
			this.registrer.setText("Fjern");
		}
		else {
			this.registrer.setText("Registrer");
		}
	}
	
	@FXML
	private void setCourseInput() {
		if (this.inputIsNTNU) {
			this.courseNameTxt.setVisible(false);
			this.courseName.setVisible(false);
			this.courseCreditTxt.setVisible(false);
			this.courseCredit.setVisible(false);
		}
		else {
			this.courseNameTxt.setVisible(true);
			this.courseName.setVisible(true);
			this.courseCreditTxt.setVisible(true);
			this.courseCredit.setVisible(true);
		}
	}
	

	protected void importFromFile(String name, boolean showMessage) {
		try {
			this.courseRegister = (CourseRegister) filehandler.readRegister(name);
			updateListView();
			updateAverage();
			if (showMessage)
				HelperFunctions.showMessageBox("Registeret er importert.", "Filhåndtering");
		}
		catch (Exception e) {
			logger.write(e.toString());
			if (showMessage)
				HelperFunctions.showErrorBox("Ups. Kunne ikke importere fra fil."
						+ "\nDette kan skyldes at filen er skadet eller ikke finnes.");
			this.courseRegister = new CourseRegister(name);
		}
	}
	
	@FXML
	protected void saveToFile() {
		
		try {
			filehandler.saveRegister(courseRegister, courseRegister.getName());
		} catch (IOException e) {
			logger.write(e.toString());
		}
		
		HelperFunctions.showMessageBox("Registeret er lagret.", "Filhåndtering");
	}
	
	
	protected void clearContents(String name) {
		if (HelperFunctions.showConfirmBox("Er du sikker på at du vil slette registeret: " + name + "?"
				+ "\nDette sletter også filen knyttet til registeret.")) {
			this.txt1.setText(setHeader());
			this.courseRegister = new CourseRegister();
			this.average.setText("");
			this.txtCourseCredit.setText("Studiepoeng: ");
			updateAverage();
			try {
				filehandler.deleteFile(name);
			}
			catch (Exception e) {
				logger.write(e.toString());
			}
			logger.write("Registeret er slettet");
		}
		
	}
	

    // Setter lengden på kolonnene
	private int c1 = 11;
	private int c2 = 46;
	private int c3 = 12;
	private int c4 = 13;

	@FXML
	private void updateListView() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(setHeader());
	
			Iterator<Icourse> iterator = courseRegister.getCourses(settings).iterator();
			
			
			while (iterator.hasNext()) {
				Icourse currentCourse = iterator.next();
				sb.append(HelperFunctions.fixedLengthString(currentCourse.getCourseID(), c1));
				sb.append(HelperFunctions.fixedLengthString(currentCourse.getCourseName(), c2));
				sb.append(HelperFunctions.fixedLengthString(currentCourse.getCourseResultString(), c3));
				sb.append(HelperFunctions.fixedLengthString(currentCourse.getOverUnderAverage(), c4));
				sb.append("\n");
			}
			txt1.setText(sb.toString());
			this.txtCourseCredit.setText("Studiepoeng: " + String.valueOf(courseRegister.getCourseCredit()));
			
//			Dette hånterer en feil dersom første inntasting ikke har en over/under atributt
			try {
				if (this.courseRegister.getCourses().size() != 0)
					this.averageOverUnder.setText("Over/under snitt: " + String.valueOf(HelperFunctions.round(courseRegister.getOverUnderAverage(),2)));
				else
					this.averageOverUnder.setText("");
			}
			catch (Exception e) {
				logger.write("Kan ikke beregne over/under snitt");
			}
			
			txtRegName.setText("Registernavn: " + this.courseRegister.getName());
			checkInput();
			updateAverage();
		}
		catch (Exception e) {

			logger.write(e.toString());
		}
		
	}
	
	private String setHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append(HelperFunctions.fixedLengthString("Fagkode:", c1));
		sb.append(HelperFunctions.fixedLengthString("Fagnavn:", c2));
		sb.append(HelperFunctions.fixedLengthString("Resultat:", c3));
		sb.append(HelperFunctions.fixedLengthString("Over/under:", c4));
		sb.append("\n" + "\n");
		return sb.toString();
	}

	
	@FXML
	private void updateAverage() {
		StringBuilder sb = new StringBuilder();
		if (this.courseRegister.getCourses().size() != 0) {
			
			sb.append("Gjennomsnitt: ");
			sb.append(HelperFunctions.round(courseRegister.getAverage(), 2));
			average.setText(sb.toString());
		}
		else {
			average.setText("");
		}
	}
	
	protected void setRegisterName(String name) {
		if (!this.courseRegister.getName().equals(name)) {
			this.courseRegister.setName(name);
			// Trying to load register based on name
			try {
				importFromFile(name, false);
			}
			catch (Exception e) {
				logger.write(e.toString());
			}
		}
		
		
		
	}
	
	protected String getRegisterName() {
		return this.courseRegister.getName();
	}

	
	private GradeCalcSettingsController settingsController;
	
	private Scene scene;
	private Parent oldSceneRoot, settingsSceneRoot;
	
	
	@FXML
	private void handleSettingsAction() {
		if (settingsController == null) {
			final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingsPage.fxml"));
			try {
				// load settings ui
				settingsSceneRoot = fxmlLoader.load();
				// remember old ui
				scene = anchorPane.getScene();
				oldSceneRoot = scene.getRoot();
				
				settingsController = fxmlLoader.getController();
				settingsController.setGradeCalcController(this);
				
			}
			catch (Exception e) {
				logger.write(e.toString());
			}
		}
		if (settingsController != null) {
			anchorPane.getScene().setRoot(settingsSceneRoot);

		}
		
		
	}
	
	protected void goBack() {
		scene.setRoot(oldSceneRoot);
		
	}
	
	protected void applySettings(final GradeCalcSettings settings) {
		if (settings != null) {
			settings.copyInto(this.settings);

			try {
				settingsFilehandler.saveSettings(settings);
			} catch (IOException e) {
				logger.write(e.toString());
			}
		}

		if (settings != null) {
			updateListView();
		}
		
		switch (settings.getFileHandling()) {
		case TXT_BASED: {
			filehandler = new GradeCalcFileHandler();
			break;
		}
		case SERIALIZABLE:
			filehandler = new GradeCalcFileHandlerSer();
			break;
		}

	}

}
