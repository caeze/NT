package filehandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import console.Log;
import preferences.Preferences;
import view.View;
import view.l10n.L10n;

/**
 * File handling util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class FileHandlingUtil {

	private static FileHandlingUtil instance;

	private FileHandlingUtil() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static FileHandlingUtil getInstance() {
		if (instance == null) {
			instance = new FileHandlingUtil();
		}
		return instance;
	}

	public String showSaveFileSelector(String fileExtension) {
		final JFileChooser fc = new JFileChooser(Preferences.getInstance().lastOpenedProjectPath);
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter(fileExtension + " " + L10n.getString("documents"), fileExtension));
		if (fc.showSaveDialog(View.getInstance()) == JFileChooser.APPROVE_OPTION) {
			Preferences.getInstance().lastOpenedProjectPath = fc.getSelectedFile().getAbsoluteFile().toString();
			return fc.getSelectedFile().getAbsoluteFile().toString();
		}
		return null;
	}

	public File showOpenFileSelector(String... fileExtensions) {
		final JFileChooser fc = new JFileChooser(Preferences.getInstance().lastOpenedProjectPath);
		fc.setAcceptAllFileFilterUsed(false);
		String extensions = "";
		for (String e : fileExtensions) {
			if (!extensions.isEmpty()) {
				extensions += ", ";
			}
			extensions += e.toUpperCase();
		}
		fc.addChoosableFileFilter(new FileNameExtensionFilter(extensions + " " + L10n.getString("documents"), fileExtensions));
		if (fc.showOpenDialog(View.getInstance()) == JFileChooser.APPROVE_OPTION) {
			Preferences.getInstance().lastOpenedProjectPath = fc.getSelectedFile().getAbsoluteFile().toString();
			return fc.getSelectedFile().getAbsoluteFile();
		}
		return null;
	}

	public String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), Charset.forName("UTF-8"));
        } catch (Exception e) {
            Log.error(FileHandlingUtil.class, "Error reading file " + filePath + ": " + e.getMessage());
        }
        return null;
    }

    public void writeStringToFile(String filePath, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            Log.error(FileHandlingUtil.class, "Error writing to file " + filePath + ": " + e.getMessage());
        }
    }

	public String getExtension(String fileName) {
		char ch;
		int len;
		if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\' || ch == '.') {
			return "";
		}
		int dotInd = fileName.lastIndexOf('.');
		int sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		}
		return fileName.substring(dotInd + 1).toLowerCase();
	}
}
