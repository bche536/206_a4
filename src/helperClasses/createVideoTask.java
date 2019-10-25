package application;

import javafx.concurrent.Task;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public class createVideoTask extends Task<Void> {
    private String _searchTerm, _creationName, _path;
    private int _num;

    public createVideoTask(String creationName, String searchTerm, String pathToAudio, int num){
        _searchTerm = searchTerm;
        _creationName = creationName;
        _path = pathToAudio;
        _num = num;
    }

    @Override
    protected Void call() throws Exception {
        Thread.sleep(300);
        try {
            File file = new File(_path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames+0.0) / format.getFrameRate();

            //Calculate the frame rate for equal distribution of screen time for each image in the video
            double pictureDuration = _num/durationInSeconds;

            String cmd = "cat " + _path + "/temp/*.jpg | ffmpeg -f image2pipe -framerate " + pictureDuration + " -i - -i " + _path
                    + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=500:500, drawtext=fontfile=./Raleway-RegularItalic.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=\\'\"" + _searchTerm + "\"\\'\" -r 25 -max_muxing_queue_size 1024 -y "
                    + _path + "/" + _creationName;
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            Process process = pb.start();


        } catch (IOException e) {

        }
        return null;
    }
}