package helperClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToTxtFile {

    /**
     * Writes output of search a text file
     * @param path
     * @param fileNameWithExtension
     * @param txt
     * @throws IOException
     */
    public static void writeTxt(String path, String fileNameWithExtension, String txt) throws IOException {
        File file = new File(path + "/" + fileNameWithExtension);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(txt.replaceAll("[\\[\\](){}']", ""));
        bw.close();
    }
}
