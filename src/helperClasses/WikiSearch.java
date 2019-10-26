package helperClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;


public class WikiSearch extends Task<Void> {
	private String _toSearch;

	public WikiSearch(String ToSearch) {
		_toSearch = ToSearch;
	}

	@Override
	protected Void call() throws Exception {
		// TODO Auto-generated method stub

		// determine whether it is valid input
		Thread.sleep(300);
		try {
			String myDirectory = "206project_team17"; // user Folder Name
			String users_home = System.getProperty("user.home");
			String path = users_home.replace("\\", "/") + File.separator + myDirectory;
			
			// search in wiki
			String cmd = "wikit " + _toSearch;
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			Process process = pb.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

			List<String> content = new ArrayList<String>();
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				content.add(line);
			}

			File file1 = new File(path + "/" + "textFromWiki.txt");
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
			for (String str : content) {
				bw1.write(str);
			}
			bw1.close();

		} catch (IOException e) {

		}
		return null;
	}

}
