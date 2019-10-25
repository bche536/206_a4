package resources;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class introController implements Initializable {
    @FXML
    private ImageView introImg;

    @FXML
    private Button introBtn;

    @FXML
    private ImageView introTxt;

    @FXML
    void beginClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) introBtn.getScene().getWindow();
        Scene scene = new Scene(loader.getRoot());
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }





}
