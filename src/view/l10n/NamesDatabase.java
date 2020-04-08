package view.l10n;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import console.Log;

/**
 * Names database.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class NamesDatabase {

	private static NamesDatabase instance;

	private List<String> firstNamesCache = new ArrayList<>();
	private List<String> lastNamesCache = new ArrayList<>();

	private NamesDatabase() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static NamesDatabase getInstance() {
		if (instance == null) {
			instance = new NamesDatabase();
		}
		return instance;
	}

	public boolean isFirstName(String s) {
		if (firstNamesCache.isEmpty()) {
			fillCache("firstNames.txt", firstNamesCache);
		}
		return firstNamesCache.contains(s.toLowerCase());
	}

	public boolean isLastName(String s) {
		if (lastNamesCache.isEmpty()) {
			fillCache("lastNames.txt", lastNamesCache);
		}
		return lastNamesCache.contains(s.toLowerCase());
	}

	private void fillCache(String fileName, List<String> target) {
		try {
			if (fileName != null && !fileName.isEmpty() && NamesDatabase.class.getResource(fileName) != null) {
				InputStream in = NamesDatabase.class.getResourceAsStream(fileName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					target.add(line);
				}
			} else {
				throw new Exception("File: " + fileName + " not found in jar!");
			}
		} catch (Exception e) {
			Log.error(NamesDatabase.class, "Error on constructing names cache: " + e.getMessage());
		}
	}
}
