package resources;

import helperClasses.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class mainController implements Initializable {
    //---------------------------------- Initialise helper classes ---------------------------------
    private AlertGenerator _alertGenerator = new AlertGenerator();
    private Alert _alert;

    private ExecutorService _flickrService;

    //---------------------------------- Query fields ----------------------------------------------
    private String _path, _searchTerm, _searchText, _audioFilePath, _audioFileName;
    private TextInputDialog audioFileNamePrompt;

    //---------------------------------- General fields --------------------------------------------
    private boolean _selectedPressed;


    @FXML private Tab myCreations;

    /**************************** START OF EXISTING CREATIONS TAB COMPONENTS **********************************/

    @FXML private Button creationsPlayBtn;
    @FXML private Button creationsDeleteBtn;
    @FXML private Button creationsMediaPlayBtn;
    @FXML private Button creationsMediaPauseBtn;
    @FXML private Button creationsMediaStopBtn;
    @FXML private StackPane mediaHolder;
    @FXML private MediaView myCreationsPlayer;
    @FXML private ListView existingCreationsList;
    private MediaPlayer _existingCreationsMediaPlayer;

    /**************************** START OF EXISTING CREATIONS TAB COMPONENTS **********************************/


    /**************************** START OF CREATION TAB COMPONENTS **********************************/

    /* ========================== START OF SEARCH PANE COMPONENTS ================================= */

    @FXML private AnchorPane searchTextPane;
    @FXML private Button searchHelpBtn;
    @FXML private TextField searchField;
    @FXML private Label searchingLabel;
    @FXML private Button searchBtn;
    @FXML private TextArea searchTextArea;
    @FXML private ProgressBar searchProgress;
    @FXML private TextArea selectedTextArea;
    @FXML private Button addTextBtn;
    @FXML private Button nextSearchBtn;

    /* ========================== END OF SEARCH PANE COMPONENTS ================================= */

    /* ========================== START OF AUDIO PANE COMPONENTS ================================= */

    @FXML private AnchorPane createAudioPane;
    @FXML private TextArea audioDisplayText;
    @FXML private ChoiceBox<String> audioChoiceBox;
    @FXML private ChoiceBox<String> backgroundChoiceBox;
    @FXML private Button createAudioBtn;
    @FXML private ListView<String> audioCreationsList;
    @FXML private Button previewAudioBtn;
    @FXML private Button stopPreviewAudioBtn;
    @FXML public static MediaPlayer previewPlayer;
    @FXML private Button combineBtn;
    @FXML private Button selectAudioBtn;
    @FXML private Button deleteAudioBtn;
    @FXML private Button audioNextBtn;
    @FXML private Label selectedAudioLabel;

    /* ========================== END OF AUDIO PANE COMPONENTS ================================= */

    /* ========================== START OF FLICKR PANE COMPONENTS ============================== */

    @FXML private AnchorPane flickrPane;
    @FXML private Label flickrSceneLabel;
    @FXML private ProgressBar flickrProgress;
    @FXML private ToggleButton imageBtn1;
    @FXML private ImageView image1;
    @FXML private ToggleButton imageBtn2;
    @FXML private ImageView image2;
    @FXML private ToggleButton imageBtn3;
    @FXML private ImageView image3;
    @FXML private ToggleButton imageBtn4;
    @FXML private ImageView image4;
    @FXML private ToggleButton imageBtn5;
    @FXML private ImageView image5;
    @FXML private ToggleButton imageBtn6;
    @FXML private ImageView image6;
    @FXML private ToggleButton imageBtn7;
    @FXML private ImageView image7;
    @FXML private ToggleButton imageBtn8;
    @FXML private ImageView image8;
    @FXML private ToggleButton imageBtn9;
    @FXML private ImageView image9;
    @FXML private ToggleButton imageBtn10;
    @FXML private ImageView image10;
    @FXML private TextField creationNameField;
    @FXML private Button finishFlickrBtn;

    private ImageView[] images;
    private ToggleButton[] imageButtons;

    /* ========================== END OF FLICKR PANE COMPONENTS ============================== */


    /**************************** END OF CREATION TAB COMPONENTS **********************************/


    /**************************** START OF REVIEW TAB COMPONENTS **********************************/

    @FXML private Button _help;
    @FXML private Button _play;
    @FXML private Button _confirmReviewOption;
    @FXML private TableView<Creation> _tableViewForReview;
    @FXML private TableColumn<Creation, String> _tableFileName; // column 1
    @FXML private TableColumn<Creation, String> _tableKeyword; // column 2
    @FXML private TableColumn<Creation, String> _confidenceLevel; // column 3
    @FXML private TableColumn<Creation, String> _numberOfPlays; // column 4

    private List<Creation> _existingCreations = new ArrayList<Creation>();;


    /**************************** END OF REVIEW TAB COMPONENTS **********************************/



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //------------------------- Create the string directory to the working folder -----------
        String myDirectory = "206project_team17"; // user Folder Name
        String users_home = System.getProperty("user.home");
        _path = users_home.replace("\\", "/") + File.separator + myDirectory;
        new File(_path).mkdir();

        //-------------------------- SET UP EXISTING CREATIONS PANE COMPONENTS -----------------------

        refreshExistingCreationsList();

        //-------------------------- SET UP SEARCH PANE COMPONENTS -----------------------------------

        searchTextPane.toFront();
        _searchTerm = "";
        searchingLabel.setVisible(false);
        searchProgress.setVisible(false);
        searchTextArea.setEditable(false);

        BooleanBinding isAddTextAreaEmpty = Bindings.isEmpty(searchTextArea.textProperty());
        addTextBtn.disableProperty().bind(isAddTextAreaEmpty);

        //-------------------------- SET UP AUDIO PANE COMPONENTS ----------------------------

        try {
            generateScm();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioChoiceBox.getItems().add("Default Voice");
        audioChoiceBox.getItems().add("AKL Accent");
        audioChoiceBox.setValue("Default Voice");

        backgroundChoiceBox.getItems().add("forgottenland");
        backgroundChoiceBox.getItems().add("magicyworld");
        backgroundChoiceBox.getItems().add("newyorkcity");
        backgroundChoiceBox.getItems().add("no background music");
        backgroundChoiceBox.setValue("forgottenland");

        audioFileNamePrompt = new TextInputDialog();
        audioFileNamePrompt.setHeaderText("Enter a name for audio file");
        audioFileNamePrompt.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        audioCreationsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        refreshAudioList();

        //-------------------------- SET UP FLICKR PANE COMPONENTS ------------------------

        images = new ImageView[10];
        images[0] = image1;
        images[1] = image2;
        images[2] = image3;
        images[3] = image4;
        images[4] = image5;
        images[5] = image6;
        images[6] = image7;
        images[7] = image8;
        images[8] = image9;
        images[9] = image10;

        imageButtons = new ToggleButton[10];
        imageButtons[0] = imageBtn1;
        imageButtons[1] = imageBtn2;
        imageButtons[2] = imageBtn3;
        imageButtons[3] = imageBtn4;
        imageButtons[4] = imageBtn5;
        imageButtons[5] = imageBtn6;
        imageButtons[6] = imageBtn7;
        imageButtons[7] = imageBtn8;
        imageButtons[8] = imageBtn9;
        imageButtons[9] = imageBtn10;


        //-------------------------- SET UP REVIEW TAB COMPONENTS -------------------------

        // set up table view
        _tableViewForReview.getItems().setAll(this._existingCreations);
        _tableFileName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        _tableKeyword.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKeyword()));
        _numberOfPlays
                .setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getNumberOfPlays())));
        _confidenceLevel.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getConfidenceLevel()));
        editableConfidence();
    }

    /*************************** START OF EXISTING CREATIONS TAB LOGIC ***********************************/


    @FXML void myCreationsHelpBtnPressed(ActionEvent event){

    }

    @FXML void creationsPlayBtnPressed(ActionEvent event) {
        try {
            String selectedFile = existingCreationsList.getSelectionModel().getSelectedItem().toString();
            if(selectedFile == null){
                //do nothing
                return;
            }
            String path = _path + "/" + selectedFile + ".mp4";
            Media media = new Media(new File(path).toURI().toString());
            _existingCreationsMediaPlayer = new MediaPlayer(media);
            myCreationsPlayer.setMediaPlayer(_existingCreationsMediaPlayer);
            _existingCreationsMediaPlayer.setAutoPlay(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML void creationsDeleteBtnPressed(ActionEvent event) {
        try {
            String selectedFile = existingCreationsList.getSelectionModel().getSelectedItem().toString();
            if(selectedFile == null){
                //do nothing
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedFile + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            _alert = _alertGenerator.newAlert("File deletion", "Please confirm", "Do you want to delete the creation " + selectedFile + "?", "confirmation");

            if (alert.getResult() == ButtonType.YES) {
                String cmd = "rm -f " + _path + "/" + selectedFile + ".mp4";
                ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
                Process process = builder.start();
                process.waitFor();
                process.destroy();
                refreshExistingCreationsList();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @FXML void creationsMediaPauseBtnPressed(ActionEvent event) {
        _existingCreationsMediaPlayer.pause();
    }

    @FXML void creationsMediaPlayBtnPressed(ActionEvent event) {
        _existingCreationsMediaPlayer.play();
    }

    @FXML void creationsMediaStopBtnPressed(ActionEvent event) {
        _existingCreationsMediaPlayer.stop();
    }

    /*************************** END OF EXISTING CREATIONS TAB LOGIC ***********************************/


    /*************************** START OF CREATION TAB LOGIC ***********************************/


    /*===========================START OF SEARCH PANE LOGIC  ==================================*/

    //------------------------------- searchBtn Button Logic ---------------------------------

    @FXML void searchBtnPressed(ActionEvent event) throws Throwable {
        //When the button is clicked, Get the search term
        _searchTerm = searchField.getText();
        if (_searchTerm.isEmpty() || _searchTerm.trim().isEmpty() || !isAlphanumeric(_searchTerm) || !isAlphanumeric2(_searchTerm)) {

            // Show alert if search term is empty
            _alert = _alertGenerator.newAlert("Invalid Input", "Invalid search term", "Please enter a valid term to search", "error");
            _alert.showAndWait();

        } else {
            WikiSearch searchWikiTask = new WikiSearch(_searchTerm);

            // While the task is running
            searchWikiTask.setOnRunning((succeesesEvent) -> {
                searchProgress.setVisible(true);
                searchingLabel.setText("Searching please wait...");
                // disable the button for creation
                searchBtn.setDisable(true);
                searchingLabel.setVisible(true);
            });

            searchProgress.progressProperty().bind(searchWikiTask.progressProperty());

            // When the thread finished its task
            searchWikiTask.setOnSucceeded((succeededEvent) -> {
                try {
                    searchProgress.setVisible(false);
                    searchingLabel.setText("Search is complete");
                    searchBtn.setDisable(false);

                    String cmd = "cat " + _path + "/" + "textFromWiki.txt";
                    ProcessBuilder searchWikiPb = new ProcessBuilder("bash", "-c", cmd);
                    Process searchWikiProcess = searchWikiPb.start();
                    InputStream searchWikiStdout = searchWikiProcess.getInputStream();
                    BufferedReader searchWikiStdoutBuffered = new BufferedReader(new InputStreamReader(searchWikiStdout));
                    String searchWikiLine = searchWikiStdoutBuffered.readLine();
                    String temp = _searchTerm + " not found :^(";

                    searchWikiProcess.waitFor();
                    searchWikiProcess.destroy();

                    searchProgress.progressProperty().unbind();
                    searchProgress.setProgress(0);

                    if (searchWikiLine.equals(temp)) {
                        // Invalid input, display an alert
                        _alert = _alertGenerator.newAlert("Invalid Input", "Invalid Term", "No results available, please enter a valid input", "error");
                        _alert.showAndWait();

                    } else {
                        try {
                            String getTextCmd = "cat " + _path + "/" + "textFromWiki.txt";
                            ProcessBuilder getTextPb = new ProcessBuilder("bash", "-c", getTextCmd);
                            Process getTextProcess = getTextPb.start();
                            InputStream getTextStdout = getTextProcess.getInputStream();
                            BufferedReader getTextStdoutBuffered = new BufferedReader(new InputStreamReader(getTextStdout));
                            String getTextLine = null;
                            _searchText = "";
                            while ((getTextLine = getTextStdoutBuffered.readLine()) != null) {
                                _searchText = _searchText + getTextLine + "\n";
                            }
                            getTextProcess.waitFor();
                            getTextProcess.destroy();
                        } catch (IOException getTextException) {
                            getTextException.printStackTrace();
                        }
                        searchTextArea.setText(_searchText);
                        _searchText = "";
                    }
                } catch (Exception searchWikiException) {
                    searchWikiException.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            });

            ExecutorService searchService = Executors.newFixedThreadPool(1);
            searchService.execute(searchWikiTask);
            searchService.shutdown();
        }
    }

    //--------------------------------- addTextBtn Logic --------------------------------------

    @FXML void addBtnPressed(ActionEvent event) {
        String selectedText = searchTextArea.getSelectedText();
        selectedTextArea.setText(selectedText);
    }

    //--------------------------------- previewSelectedBtn Logic --------------------------------------

    @FXML void previewSelectedBtnPressed(ActionEvent event) throws Throwable {
        // play the selected text
        String str = selectedTextArea.getText();
        int words = getWordCounts(str);

        if (selectedTextArea.getText().isEmpty() || selectedTextArea.getText().trim().isEmpty()) {
            //do nothing
        } else if (words < 10 || words > 40) {
            //Selected text has too little/many words
            _alert = _alertGenerator.newAlert("Invalid Input", "Invalid number of words", "Please select 10-40 words only", "error");
            _alert.showAndWait();

        } else {
            String content = selectedTextArea.getText().replaceAll("[\\[\\](){}']", "");
            try {
                String previewCmd = "echo " + content + " | festival --tts";
                ProcessBuilder previewPb = new ProcessBuilder("bash", "-c", previewCmd);
                Process previewProcess = previewPb.start();
                previewProcess.waitFor();
                previewProcess.destroy();

            } catch (IOException previewException) {
                previewException.printStackTrace();
            }
        }
    }


    //--------------------------------- clearSelectionBtn Logic --------------------------------

    @FXML void clearSelectionBtnPressed(ActionEvent event) {
        selectedTextArea.clear();
    }

    //--------------------------------- searchNextBtn Logic --------------------------------

    @FXML void searchNextBtnPressed(ActionEvent event) throws Throwable {
        audioDisplayText.setText(selectedTextArea.getText());
        if(getWordCounts(selectedTextArea.getText()) < 41 && getWordCounts(selectedTextArea.getText()) > 9) {
            createAudioPane.toFront();
        }
        else{
            _alert = _alertGenerator.newAlert("Invalid Input", "Invalid word count", "Please have 10 - 40 words only in your selected text area", "error");
            _alert.showAndWait();
        }
    }

    //--------------------------------- searchHelpBtn Logic -----------------------------------------

    @FXML void searchHelpBtnPressed(ActionEvent event) {
        //NEED TO SHOW INSTRUCTION ALERT
    }


    /*=========================== END OF SEARCH PANE LOGIC ==================================*/


    /*=========================== START OF AUDIO PANE LOGIC ===============================*/



    //--------------------------------- createAudioBtn Logic ----------------------------------

    @FXML void createAudioBtnPressed(ActionEvent event) throws Throwable {
        String synthesizer = audioChoiceBox.getValue();
        //String bgm = backgroundChoiceBox.getValue();

        audioFileNamePrompt.showAndWait();
        String audioName = audioFileNamePrompt.getEditor().getText();

        // determine whether it is a valid name
        if (audioName.isEmpty() || audioName.trim().isEmpty() || !isAlphanumeric2(audioName) || !isAlphanumeric(audioName)) {
            _alert = _alertGenerator.newAlert("Invalid name", "Empty input", "Please enter a valid name for this audio file", "error");
            _alert.showAndWait();

        } else {
            if (isValidName(audioName)) {
                try {
                    // Write the selected text to a .txt file
                    WriteToTxtFile.writeTxt(_path, "Selected.txt", selectedTextArea.getText());

                } catch (Exception writingSelectedTextException) {
                    writingSelectedTextException.printStackTrace();
                }
                // create the audio
                if (synthesizer == "AKL Accent") {
                    try {
                        AudioCreationTask audioTask = new AudioCreationTask(_path, backgroundChoiceBox.getValue(), audioName, synthesizer);

                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.submit(audioTask);

                        if (isCreated() == false) {
                            // The audio file they created has some unpronounceable words for this synthesiser
                            _alert = _alertGenerator.newAlert("Error", "Unreadable words", "Text contains unreadable words, please change to the default voice or edit the text", "error");
                            _alert.showAndWait();

                            String cmd1 = "rm " + _path + "/" + audioName + ".wav";
                            ProcessBuilder pb1 = new ProcessBuilder("bash", "-c", cmd1);
                            Process process1 = pb1.start();
                            process1.waitFor();
                            process1.destroy();

                        } else {

                            // show confirmation
                            audioTask.setOnSucceeded((onCompletion) ->{
                                executorService.shutdown();
                                // Alert the user that the audio file has been successfully created
                                try {
                                    _alert = _alertGenerator.newAlert("Audio completed", "Audio file generated", "The audio file " + audioName + " has been made", "information");
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                                _alert.showAndWait();
                                refreshAudioList();
                            });
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    refreshAudioList();

                } else {
                    // use the default synthesizer
                    AudioCreationTask audioTask = new AudioCreationTask(_path, backgroundChoiceBox.getValue(), audioName, synthesizer);

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(audioTask);

                    audioTask.setOnSucceeded((onCompletion) ->{
                        executorService.shutdown();
                        // Alert the user that the audio file has been successfully created
                        try {
                            _alert = _alertGenerator.newAlert("Audio completed", "Audio file generated", "The audio file " + audioName + " has been made", "information");
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        _alert.showAndWait();
                        refreshAudioList();
                    });

                }

            } else {
                //Audio file already exists so alert the user
                _alert = _alertGenerator.newAlert("Invalid input", "Duplicate file", "File name already exists, enter another name", "error");
                _alert.showAndWait();

            }
        }
    }

    //--------------------------------- audioBackBtn Logic --------------------------------------


    @FXML void audioBackBtnPressed(ActionEvent event) {
        searchTextPane.toFront();
    }

    //--------------------------------- audioHelpBtnPressed Logic --------------------------------------

    @FXML void audioHelpBtnPressed(ActionEvent event) {

    }

    //--------------------------------- combineBtn Logic --------------------------------------

    @FXML void combineBtnPressed(ActionEvent event) throws Throwable {
        ObservableList<String> selectedItems = audioCreationsList.getSelectionModel().getSelectedItems();
        String audioFileNames = "";
        int count = 0;
        // must be multiple selection
        for (String s : selectedItems) {
            audioFileNames = audioFileNames + _path + "/" + s + ".wav ";
            count++;

        }
        if (count > 1) {

            audioFileNamePrompt.showAndWait();
            String audioName = audioFileNamePrompt.getEditor().getText();

            // Determine whether it is a valid name
            if (audioName.isEmpty() || audioName.trim().isEmpty()) {
                _alert = _alertGenerator.newAlert("Invalid input", "Invalid FIle Name", "Please enter a non empty file name", "error");
                _alert.showAndWait();

            } else {
                if (isValidName(audioName)) {

                    // combine wav files
                    try {
                        String soxCmd = "sox " + audioFileNames + _path + "/" + audioName + ".wav";

                        ProcessBuilder pb = new ProcessBuilder("bash", "-c", soxCmd);
                        Process process = pb.start();
                        process.waitFor();
                        process.destroy();

                    } catch (IOException invalidSoxCmdException) {
                        invalidSoxCmdException.printStackTrace();
                    }
                    // Alert the user that the audio file has been successfully created
                    _alert = _alertGenerator.newAlert("Audio completed", "Audio file generated", "THe audio file" + audioName + " has been made", "information");
                    _alert.showAndWait();
                    refreshAudioList();

                } else {
                    //Audio file already exists so alert the user
                    _alert = _alertGenerator.newAlert("Invalid input", "Duplicate file", "File name already exists, enter another name", "error");
                    _alert.showAndWait();

                }

            }
        }
        else {
            //The user has tried to combine less than 2 audio files, alert the user
            _alert = _alertGenerator.newAlert("Invalid Selection", "Not enough files selected", "Please highlight at least 2 audio files to combine", "error");
            _alert.showAndWait();

        }
        refreshAudioList();
    }

    //--------------------------------- previewAudioBtn Logic ---------------------------------

    @FXML void previewAudioBtnPressed(ActionEvent event) {
        String fileName = audioCreationsList.getSelectionModel().getSelectedItem();

        if(fileName == null){
            return;
        }
        PreviewAudio.previewAudio(_path, fileName);
    }

    //--------------------------------- stopPreviewAudioBtn Logic ---------------------------------

    @FXML void stopPreviewBtnPressed(ActionEvent event) {
        previewPlayer.stop();
    }

    //--------------------------------- selectAudioBtn Logic ----------------------------------

    @FXML void selectAudioBtnPressed(ActionEvent event) throws Throwable {
        if(getSelectedAudioCreationsCount() > 1){
            _alert = _alertGenerator.newAlert("Invalid Selection", "Too many files", "Please select one file only for final creation", "error");
            _alert.showAndWait();

        }
        else {
            //getSelectedAudioCreationsCount() method updates the _audioFileName field which is the location of the audioFile to be used
            String splitName[] = _audioFileName.split("/");
            String displayFileName = splitName[splitName.length - 1];
            selectedAudioLabel.setText("You have selected the audio file: " + displayFileName);
            _selectedPressed = true;
        }
    }

    //--------------------------------- deleteAudioBtn logic ---------------------------------

    @FXML void deleteAudioBtnPressed(ActionEvent event) {
        ObservableList<String> selectedItems = audioCreationsList.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            //Do nothing
        }
        else {
            for (String s : selectedItems) {
                File file = new File(_path + File.separator + s + ".wav");
                file.delete();

            }
            refreshAudioList();

        }
    }



    //--------------------------------- audioNextBtn Logic -----------------------------------------

    @FXML void audioNextBtnPressed(ActionEvent event) throws Throwable {
        if(_selectedPressed) {
            flickrProgress.setVisible(true);
            finishFlickrBtn.setDisable(true);
            flickrPane.toFront();
            //When input is valid, make a new task and submit to ExecutorServices and wait
            FlickrClass flickrTask = new FlickrClass(_searchTerm, _path);
            _flickrService = Executors.newSingleThreadExecutor();
            _flickrService.submit(flickrTask);

            flickrTask.setOnRunning((whileRunning) ->{
                for(int i = 0; i < images.length; i++){
                    images[i].setVisible(false);
                }
                flickrSceneLabel.setText("Searching for images, please wait...");
                flickrProgress.progressProperty().bind(flickrTask.progressProperty());
            });

            //When the thread finished its task prompt the user for a name for the file
            flickrTask.setOnSucceeded((succeededEvent) -> {
                _flickrService.shutdown();
                for(int i = 0; i < images.length; i++){
                    images[i].setVisible(true);
                }
                flickrSceneLabel.setText("Please click on the images that you would like in your creation");
                flickrProgress.progressProperty().unbind();
                flickrProgress.setVisible(false);
                finishFlickrBtn.setDisable(false);
                updateImages();
            });
        }
        else {
            _alert = _alertGenerator.newAlert("Nothing selected", "Please choose an audio file", "Select an audio file by highlighting a file on the list and pressing select", "error");
            _alert.showAndWait();
        }
    }

    /*=========================== END OF AUDIO PANE LOGIC ===============================*/

    /*=========================== START OF FLICKR PANE LOGIC ===============================*/

    //--------------------------------- finishFlickrBtn Logic -----------------------------------------

    @FXML void finishFlickrBtnPressed(ActionEvent event) throws Throwable {
        String cmd = "test -e " + _path + "/" + creationNameField.getText() +".mp4";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        Process process = builder.start();
        process.waitFor();
        int exitVal = process.exitValue();
        process.destroy();

        if(creationNameField.getText().isEmpty() || !isAlphanumeric(creationNameField.getText()) || !isAlphanumeric2(creationNameField.getText())){
            _alert = _alertGenerator.newAlert("Invalid Input", "Invalid characaters", "Please enter a name for this creation that is alphanumeric", "error");
            _alert.showAndWait();
        }
        else if(exitVal == 0){
            _alert = _alertGenerator.newAlert("Invalid input", "Duplicate file", "The creation name you have entered already exists", "error");
            _alert.showAndWait();
        }
        else {
            if(getSelectedImages() == null) {
                _alert = _alertGenerator.newAlert("No images", "No images selected", "You have not selected any images, do you want to create a blank creation?", "confirmation");
                _alert.showAndWait();
                if(_alert.getResult() == ButtonType.YES){
                    //create video with no images
                }
                else{
                    //do nothing
                }
            }
            else {
                int num = getSelectedImages().size();
                System.out.println(num);
                removeUnselectedImages();
                _audioFileName = _audioFileName.substring(0, _audioFileName.length() - 1);
                CreateVideoTask videoTask = new CreateVideoTask(creationNameField.getText(), _searchTerm, _audioFileName, _path, num);

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(videoTask);

                videoTask.setOnRunning((whileRunning) -> {
                    flickrProgress.setVisible(true);
                    flickrProgress.progressProperty().bind(videoTask.progressProperty());
                    flickrSceneLabel.setText("Generating video please wait...");
                });

                videoTask.setOnCancelled((onCancel) -> {
                    String cancelCmd = "rm -r " + _path + "/temp/";
                    ProcessBuilder cancelBuilder = new ProcessBuilder("bash", "-c", cmd);
                    try {
                        Process cancelProcess = cancelBuilder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    flickrProgress.setVisible(false);
                    flickrProgress.progressProperty().unbind();
                    flickrSceneLabel.setText("Video creation interrupted, please try again");
                });

                videoTask.setOnSucceeded((whenFinished) -> {
                    executorService.shutdown();

                    flickrProgress.setVisible(false);
                    flickrProgress.progressProperty().unbind();
                    flickrSceneLabel.setText("Video creation is complete");

                    Creation creation = new Creation(creationNameField.getText());
                    creation.setKeyword(_searchTerm);
                    _existingCreations.add(creation);

                    try {
                        _alert = _alertGenerator.newAlert("Creation success", "Video generation successful", "Click 'Ok' to return to the beginning", "information");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    _alert.showAndWait();

                    refreshExistingCreationsList();

                    searchTextPane.toFront();
                    try {
                        clearAll();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

    }

    @FXML void flickrBackBtnPressed(ActionEvent event) throws IOException, InterruptedException {
        createAudioPane.toFront();
        String cmd = "rm -r " + _path + "/temp/";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        Process process = pb.start();
        process.waitFor();
        process.destroy();

    }

    @FXML void flickrHelpBtnPressed(ActionEvent event) {

    }

    /*=========================== END OF FLICKR PANE LOGIC ===============================*/


    /**************************** END OF CREATION TAB LOGIC ***********************************/

    //=========================================================================================



    /************************** START OF REVIEW TAB LOGIC *************************************/

    @FXML public void reviewHelpBtnPressed(){

    }

    public void editableConfidence() {

        _confidenceLevel.setCellFactory(TextFieldTableCell.forTableColumn());
        _confidenceLevel.setOnEditCommit(e -> {

            String input = e.getNewValue();

            if (input.matches("[0-9]+")) {
                if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 5) {
                    // show alert
                    try {
                        _alert = _alertGenerator.newAlert("Invalid Input", "Invalid number range", "Enter a number between 1 to 5 inclusively", "error");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Optional<ButtonType> option = _alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        _tableViewForReview.getItems().setAll(this._existingCreations);
                    }
                } else {
                    // set the new value of confidence level
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setConfidence(e.getNewValue());
                }
            } else {
                try {
                    _alert = _alertGenerator.newAlert("Invalid Input", "Invalid number range", "Enter a number between 1 to 5 inclusively", "error");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Optional<ButtonType> option = _alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    _tableViewForReview.getItems().setAll(this._existingCreations);
                }
            }

        });

    }



    /**************************** END OF REVIEW TAB LOGIC *************************************/

    //=========================================================================================

    //--------------------------------- helper functions ---------------------------------------


    public void clearAll() throws IOException, InterruptedException {
        searchField.clear();
        searchTextArea.clear();
        selectedTextArea.clear();
        _searchTerm = "";
        creationNameField.clear();
        String cmd = "rm -r " + _path + "/temp/ " + _path + "/*.wav";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        Process process = builder.start();
        process.waitFor();
        process.destroy();
        refreshAudioList();
    }

    // Counts the number of words in a string
    public int getWordCounts(String str) {
        int count=0;
        char ch[]= new char[str.length()];
        for(int i=0;i<str.length();i++)
        {
            ch[i]= str.charAt(i);
            if( ((i>0)&&(ch[i]!=' ')&&(ch[i-1]==' ')) || ((ch[0]!=' ')&&(i==0)) )  {
                count++;

            }

        }
        return count;

    }

    public void refreshAudioList() {
        List<String> fileNames = getAudioNameList();
        ObservableList<String> items = FXCollections.observableList(fileNames);
        audioCreationsList.setItems(items);
    }

    public void refreshExistingCreationsList() {
        List<String> fileNames = getVideoNameList();
        ObservableList<String> items = FXCollections.observableList(fileNames);
        existingCreationsList.setItems(items);
    }

    public List<String> getVideoNameList() {
        try {
            String cmd = "ls " + _path + "/" + " | grep mp4 | sort | cut -f1 -d'.'";
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            List<String> fileNames = new ArrayList<String>();
            String line = null;
            while ((line = stdoutBuffered.readLine()) != null) {
                fileNames.add(line);
            }
            process.waitFor();
            process.destroy();
            return fileNames;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    // Gets a list of names from existing files
    public List<String> getAudioNameList() {
        try {
            String cmd = "ls " + _path + "/" + " | grep wav | sort | cut -f1 -d'.'";
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            List<String> fileNames = new ArrayList<String>();
            String line = null;
            while ((line = stdoutBuffered.readLine()) != null) {
                fileNames.add(line);
            }
            process.waitFor();
            process.destroy();
            return fileNames;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }


    // Checks whether the name is valid
    public boolean isValidName(String InputName) {
        boolean IsExist = false;
        List<String> fileNames = getAudioNameList();
        for (int i = 0; i < fileNames.size(); i++) {
            if (InputName.equals(fileNames.get(i))) {
                IsExist = true;
                break;
            }
        }
        if (IsExist == false) {
            return true;
        } else {
            return false;
        }

    }

    // Checks if file already exists
    public boolean isCreated() throws IOException, InterruptedException {
        String cmd = "cat " + _path + "/" + "error.txt";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        Process process = pb.start();
        InputStream stdout = process.getInputStream();
        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
        String line = stdoutBuffered.readLine();
        String temp = "SIOD ERROR: could not open file /usr/share/festival/dicts/oald/oald_lts_rules.scm";
        process.waitFor();
        process.destroy();

        if (line == null) {
            return true;
        } else if (line.equals(temp)) {
            return false;
        } else {
            return true;
        }
    }

    //Generates voice files
    public void generateScm() throws IOException {
        FileWriter fw1 = new FileWriter(_path + "/" + "akl.scm");
        fw1.write("(voice_akl_nz_jdt_diphone)");
        fw1.close();

        FileWriter fw2 = new FileWriter(_path + "/" + "kal.scm");
        fw2.write("(voice_kal_diphone)");
        fw2.close();

    }

    public int getSelectedAudioCreationsCount() {
        ObservableList<String> selectedItems = audioCreationsList.getSelectionModel().getSelectedItems();
        _audioFileName = "";
        int count = 0;
        // must be multiple selection
        for (String s : selectedItems) {
            _audioFileName = _audioFileName + _path + "/" + s + ".wav ";
            count++;
        }
        System.out.println("aud file= " + _audioFileName);
        return count;
    }

    public void updateImages() {
        File imagesDir = new File(_path + "/temp");
        File[] imagesList = imagesDir.listFiles();
        for(int i = 0; i < images.length; i++){
            Image image = new Image(imagesList[i].toURI().toString());
            images[i].setImage(image);
        }
    }

    public List<Image> getSelectedImages() {
        int count = 0;
        //Initial check if no images are selected
        for(int i = 0; i < imageButtons.length; i++) {
            if(imageButtons[i].isSelected()){
                count++;
            }
        }
        if(count == 0){
            return null;
        }
        List<Image> selectedImages = new ArrayList();
        //There is at least one image selected so run the loop
        for(int i  = 0; i < imageButtons.length; i++) {
            if(imageButtons[i].isSelected()){
                selectedImages.add(images[i].getImage());
            }
        }
        return selectedImages;
    }

    public List<Image> getUnselectedImages() {
        int count = 0;
        //Initial check if no images are selected
        for(int i = 0; i < imageButtons.length; i++) {
            if(imageButtons[i].isSelected()){
                count++;
            }
        }
        if(count == 0){
            return null;
        }
        List<Image> unselectedImages = new ArrayList();
        //There is at least one image selected so run the loop
        for(int i  = 0; i < imageButtons.length; i++) {
            if(!imageButtons[i].isSelected()){
                unselectedImages.add(images[i].getImage());
            }
        }
        return unselectedImages;
    }

    public void removeUnselectedImages() throws IOException {
        List<Image> unselectedImages = getUnselectedImages();
        for(int i = 0; i < unselectedImages.size(); i++){
            String jpgPath =  unselectedImages.get(i).impl_getUrl();
            System.out.println(jpgPath.substring(5));
            File file = new File(jpgPath.substring(5));
            file.delete();
        }
    }

    public boolean isAlphanumeric2(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }
        return true;
    }

    public boolean isAlphanumeric(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c))
                return false;
        }

        return true;
    }


}



