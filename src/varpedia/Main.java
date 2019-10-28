package varpedia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private String _path;

    /**
     * Loads the stage and starts at the intro.fxml file
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/intro.fxml"));
        primaryStage.setTitle("VARpedia");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Set up the path for stop() method to delete temp files
        String myDirectory = "206project_team17"; // user Folder Name
        String users_home = System.getProperty("user.home");
        _path = users_home.replace("\\", "/") + File.separator + myDirectory;
    }

    /**
     * On application exit, remove any temporary files created
     * @throws IOException
     */
    @Override
    public void stop() throws IOException {
        String cmd = "rm -r " + _path + "/temp/ " + _path + "/*.wav";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        Process process = builder.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
