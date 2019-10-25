package resources;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import helperClasses.Creation;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

public class ReviewSceneController implements Initializable {

	@FXML
	private Button _help; 

	@FXML
	private Button _play;

	@FXML
	private Button _confirmReviewOption;
	
	@FXML
	private ComboBox<String> _ReviewOptions;

	@FXML
	private TableView<Creation> _tableViewForReview;

	@FXML
	private TableColumn<Creation, String> _fileName; // column 1

	@FXML
	private TableColumn<Creation, String> _keyword; // column 2

	@FXML
	private TableColumn<Creation, String> _confidenceLevel; // column 3

	@FXML
	private TableColumn<Creation, String> _numberOfPlays; // column 4

	
	
	
	private List<Creation> _existingCreations = new ArrayList<Creation>();;

	
	public void RankingWithConfidenceFirst(List<Creation> creations) {
		
		Collections.sort(creations, new Comparator<Object>() {

	        public int compare(Object o1, Object o2) {

	            String x1 = ((Creation) o1).getConfidenceLevel();
	            String x2 = ((Creation) o2).getConfidenceLevel();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            Integer x3 = ((Creation) o1).getNumberOfPlays();
	            Integer x4 = ((Creation) o2).getNumberOfPlays();
	            return x3.compareTo(x4);
	    }});

	}

	public void RankingWithNumberOfPlaysFirst(List<Creation> creations) {
		
		Collections.sort(creations, new Comparator<Object>() {

	        public int compare(Object o1, Object o2) {

	        	Integer x1 = ((Creation) o1).getNumberOfPlays();
	            Integer x2 = ((Creation) o2).getNumberOfPlays();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            String x3 = ((Creation) o1).getConfidenceLevel();
	            String x4 = ((Creation) o2).getConfidenceLevel();
	            return x3.compareTo(x4);
	    }});

	}
	
	

	// link to the button (ok) _confirmReviewOption
	@FXML
	public void comboAction() {

		if (_ReviewOptions.getValue().equals("confidence first")) {
		
			RankingWithConfidenceFirst(_existingCreations);
			_tableViewForReview.getItems().setAll(this._existingCreations);
		}
		if (_ReviewOptions.getValue().equals("number of plays first")) {

			RankingWithNumberOfPlaysFirst(_existingCreations);
			_tableViewForReview.getItems().setAll(this._existingCreations);
		}

	}

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/**
		 * mock data for test (should be delete later)
		 */
		Creation creation1 = new Creation("apple1");
		creation1.setKeyword("apple");
		creation1.setConfidence("2");
		creation1.increaseNumbeOfPlays();
		creation1.increaseNumbeOfPlays();
		_existingCreations.add(creation1);

		Creation creation2 = new Creation("apple2");
		creation2.setKeyword("apple");
		creation2.increaseNumbeOfPlays();
		creation2.increaseNumbeOfPlays();
		_existingCreations.add(creation2);

		Creation creation3 = new Creation("water");
		creation3.setKeyword("water");
		creation3.increaseNumbeOfPlays();
		creation3.increaseNumbeOfPlays();
		creation3.increaseNumbeOfPlays();
		creation3.increaseNumbeOfPlays();
		creation3.increaseNumbeOfPlays();
		_existingCreations.add(creation3);
		
		Creation creation4 = new Creation("apple3");
		creation4.setKeyword("apple");
		creation4.increaseNumbeOfPlays();
		creation4.increaseNumbeOfPlays();
		_existingCreations.add(creation4);
		
		/**
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		// set up combo box
		_ReviewOptions.getItems().setAll("confidence first", "number of plays first");
		_ReviewOptions.setValue("Sort by");
		

		// set up table view
		_tableViewForReview.getItems().setAll(this._existingCreations);
		_fileName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
		_keyword.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKeyword()));
		_numberOfPlays
				.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getNumberOfPlays())));
		_confidenceLevel.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getConfidenceLevel()));
		editableConfidence();
		
		
		
	}

	public void editableConfidence() {

		_confidenceLevel.setCellFactory(TextFieldTableCell.forTableColumn());
		_confidenceLevel.setOnEditCommit(e -> {

			String input = e.getNewValue();

			if (input.matches("[0-9]+")) {
				if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 5) {
					// show alert
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setHeaderText("Input not valid");
					errorAlert.setContentText("please enter a number between 1-5");
					Optional<ButtonType> option = errorAlert.showAndWait();
					if (option.get() == ButtonType.OK) {
						_tableViewForReview.getItems().setAll(this._existingCreations);
					}
				} else {
					// set the new value of confidence level
					e.getTableView().getItems().get(e.getTablePosition().getRow()).setConfidence(e.getNewValue());
				}
			} else {

				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText("Input not valid");
				errorAlert.setContentText("please enter a number between 1-5");
				Optional<ButtonType> option = errorAlert.showAndWait();
				if (option.get() == ButtonType.OK) {
					_tableViewForReview.getItems().setAll(this._existingCreations);
				}
			}

		});

	}

}
