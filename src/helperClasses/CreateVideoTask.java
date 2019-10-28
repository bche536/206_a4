package helperClasses;

import javafx.concurrent.Task;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;

public class CreateVideoTask extends Task<Void> {
    private String _searchTerm, _creationName, _pathToAudio, _dirPath;
    private int _num;

    /**
     * Creates a task with the arguments provided
     * @param creationName
     * @param searchTerm
     * @param pathToAudio
     * @param dirPath
     * @param num
     */
    public CreateVideoTask(String creationName, String searchTerm, String pathToAudio, String dirPath, int num){
        _creationName = creationName;
        _searchTerm = searchTerm;
        _pathToAudio = pathToAudio;
        _dirPath = dirPath;
        _num = num;
    }

    /**
     * Creates a video given the input arguments
     * @return
     * @throws Exception
     */
    @Override
    protected Void call() throws Exception {
        try {
            File file = new File(_pathToAudio);
            System.out.println(_pathToAudio);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames+0.0) / format.getFrameRate();

            //Calculate the frame rate for equal distribution of screen time for each image in the video
            double pictureDuration = _num/durationInSeconds;

            String cmd = "cat " + _dirPath + "/temp/*.jpg | ffmpeg -f image2pipe -framerate " + pictureDuration + " -i - -i " + _pathToAudio
                    + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=500:500, drawtext=fontfile=./Raleway-Regular.ttf:fontsize=64: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=\\'\"" + _searchTerm + "\"\\'\" -r 25 -max_muxing_queue_size 1024 -y "
                    + _dirPath + "/" + _creationName + ".mp4";
            System.out.println(cmd);
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            Process process = pb.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}