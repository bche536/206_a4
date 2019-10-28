package helperClasses;

import javafx.concurrent.Task;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class AudioCreationTask extends Task<Void> {
    private String _path, _bgmChoice, _audioName, _synth;

    /**
     * Creates a audio creation task with the given audio optios
     * @param path
     * @param bgmChoice
     * @param audio
     * @param synth
     */
    public AudioCreationTask(String path, String bgmChoice, String audio, String synth){
        _path = path;
        _bgmChoice = bgmChoice;
        _audioName = audio;
        _synth = synth;
    }

    /**
     * Checks the audio options given in the command and creates an audio file accordingly
     * @return
     * @throws Exception
     */
    @Override
    protected Void call() throws Exception {
        if(_bgmChoice.contentEquals("no background music")) {
            if (_synth == "AKL Accent") {
                String cmd = "text2wave -o " + _path + "/" + _audioName + ".wav " + _path + "/"
                        + "Selected.txt -eval " + _path + "/" + "akl.scm" + " 2> " + _path + "/"
                        + "error.txt";
                ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
                Process process = pb.start();
                process.waitFor();
                process.destroy();

                return null;
            }
            else {
                String cmd = "text2wave -o " + _path + "/" + _audioName + ".wav " + _path + "/"
                            + "Selected.txt -eval " + _path + "/" + "kal.scm";
                ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
                Process process = pb.start();
                process.waitFor();
                process.destroy();

                return null;
                }
            }
        else{
            if (_synth == "AKL Accent") {
                String cmd = "text2wave -o " + _path + "/tempaud.wav " + _path + "/"
                        + "Selected.txt -eval " + _path + "/" + "akl.scm" + " 2> " + _path + "/"
                        + "error.txt";
                ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
                Process process = pb.start();
                process.waitFor();
                process.destroy();
            } else {
                String cmd = "text2wave -o " + _path + "/tempaud.wav " + _path + "/"
                        + "Selected.txt -eval " + _path + "/" + "kal.scm";
                ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
                Process process = pb.start();
                process.waitFor();
                process.destroy();
            }

            System.out.println(_bgmChoice);

            String convCmd = "ffmpeg -i ./" + _bgmChoice + ".mp3 -acodec pcm_u8 -ar 16000 " + _path + "/bgm.wav";
            ProcessBuilder convPb = new ProcessBuilder("bash", "-c", convCmd);
            Process convProcess = convPb.start();
            convProcess.waitFor();

            File file = new File(_path + "/tempaud.wav");
            // Calculates the length of the wikit audio file to use as the arguments for the combination bash command
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames + 0.0) / format.getFrameRate();
            int end = (int) Math.floor(durationInSeconds);

            String createCmd = "sox -m " + _path + "/tempaud.wav " + _path + "/bgm.wav " + _path + "/" + _audioName + ".wav trim 0 " + end;
            ProcessBuilder createPb = new ProcessBuilder("bash", "-c", createCmd);
            Process createProcess = createPb.start();
            createProcess.waitFor();

            String cleanUpCmd = "rm " + _path + "/tempaud.wav " + _path + "/bgm.wav";
            ProcessBuilder cleanUpPb = new ProcessBuilder("bash", "-c", cleanUpCmd);
            Process cleanUpProcess = cleanUpPb.start();
            cleanUpProcess.waitFor();

            convProcess.destroy();
            createProcess.destroy();
            cleanUpProcess.destroy();

            return null;
        }
    }
}
