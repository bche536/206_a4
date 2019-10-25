package helperClasses;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import resources.mainController;

import java.io.File;

public class PreviewAudio {

    public static void previewAudio(String path, String fileName){
        fileName = fileName.replace("\n", "").replace("\r", "");
        File audioUrl = new File (path + "/" + fileName + ".wav");
        Media audio = new Media(audioUrl.toURI().toString());
        mainController.previewPlayer = new MediaPlayer(audio);
        mainController.previewPlayer.setAutoPlay(true);
    }
}
