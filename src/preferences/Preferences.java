package preferences;

import console.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Preferences util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Preferences {

    // constants
    public static final Map<String, Locale> SUPPORTED_LOCALES = new HashMap<>();
    public static final Map<String, Integer> LOG_LEVELS = new HashMap<>();

    // application specific constants
    public Locale locale = Locale.GERMAN;
    public int logLevel = Log.LOG_LEVEL_INFO;
    public boolean showConsole = false;
    public String projectLocation = "http://caeze.bplaced.net/japy/test1.japy";

    // preferences internals
    private static File preferencesFile;
    private static Preferences instance;

    private Preferences() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();

            SUPPORTED_LOCALES.put("English", Locale.ENGLISH);
            SUPPORTED_LOCALES.put("Deutsch", Locale.GERMAN);

            LOG_LEVELS.put("debug", Log.LOG_LEVEL_DEBUG);
            LOG_LEVELS.put("info", Log.LOG_LEVEL_INFO);
            LOG_LEVELS.put("warning", Log.LOG_LEVEL_WARNING);
            LOG_LEVELS.put("error", Log.LOG_LEVEL_ERROR);

            try {
                File jarFile = new File(Preferences.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                preferencesFile = new File(jarFile.getParentFile().getAbsolutePath() + File.separator + "preferences.properties");
            } catch (Exception e) {
                Log.error(Preferences.class, e.getMessage());
            }
            instance.load();
        }
        return instance;
    }

    public void persist() {
        try {
            Properties props = new Properties();
            props.setProperty("locale", locale.getLanguage());
            props.setProperty("logLevel", String.valueOf(logLevel));
            props.setProperty("showConsole", String.valueOf(showConsole));
            props.setProperty("projectLocation", projectLocation);
            OutputStream out = new FileOutputStream(preferencesFile);
            props.store(out, "NT preferences. Do not modify!");
        } catch (Exception e) {
            Log.error(Preferences.class, e.getMessage());
        }
    }

    public void load() {
        try {
            if (!preferencesFile.exists()) {
                persist();
            }
            Properties props = new Properties();
            props.load(new FileInputStream(preferencesFile));
            String localeString = props.getProperty("locale", locale.getLanguage());
            for (Locale l : SUPPORTED_LOCALES.values()) {
                if (l.getLanguage().equals(localeString)) {
                    locale = l;
                    break;
                }
            }
            logLevel = Integer.valueOf(props.getProperty("logLevel", String.valueOf(logLevel)));
            showConsole = Boolean.valueOf(props.getProperty("showConsole", String.valueOf(showConsole)));
            projectLocation = props.getProperty("projectLocation", projectLocation);
        } catch (Exception e) {
            Log.error(Preferences.class, e.getMessage());
        }
    }
}
