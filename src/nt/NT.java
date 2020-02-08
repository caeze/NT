package nt;

import console.Log;
import control.Control;
import javax.swing.UIManager;
import test.TestSuite;
import view.util.LoadingAnimation;
import view.util.SplashScreenUtil;

/**
 * Main entry point of the NT application.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class NT {

    public static enum OperatingSystem {
        WINDOWS, LINUX, MACOS
    }

    // basic constants
    public static String VERSION = "0.0.1";
    public static boolean IS_DEBUG = false;
    public static OperatingSystem OS = OperatingSystem.WINDOWS;
    public static String lastOpenedProjectFile = "";
    public static int STUDENT_IMAGE_WIDTH = 32;
    public static int STUDENT_IMAGE_HEIGHT = 32;

    /**
     * Main entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // determine operating system
        OS = getOperatingSystem();

        // set corresponding look and feel for the whole application
        setLookAndFeel();

        // show a splash screen
        SplashScreenUtil.showSplashScreen("NTLogo.png");
        LoadingAnimation.showLoadingAnim();

        // run unit tests, but only if in debug mode
        if (IS_DEBUG) {
            if (!TestSuite.startTests()) {
                Log.error(NT.class, "Unit tests not successful! Aborting!");
                return;
            } else {
                Log.info(NT.class, "Unit tests successful.");
            }
        } else {
            Log.info(NT.class, "Debug mode off, unit tests not run.");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                // set up the program and configuration and start
                Control.getInstance().init();
                
                // kill splash screen
                SplashScreenUtil.killSplashScreen();
                LoadingAnimation.killLoadingAnim();
            }
        }).start();
    }

    private static OperatingSystem getOperatingSystem() {
        String os = System.getProperty("os.name", "generic").toLowerCase();
        if ((os.contains("mac")) || (os.contains("darwin"))) {
            return OperatingSystem.MACOS;
        }
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS;
        }
        return OperatingSystem.LINUX;
    }

    private static void setLookAndFeel() {
        switch (OS) {
            case WINDOWS:
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    Log.error(NT.class, e.getMessage());
                }
                break;
            case MACOS:
                try {
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "NT");
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    Log.error(NT.class, e.getMessage());
                }
                break;
            case LINUX:
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                } catch (Exception e) {
                    Log.error(NT.class, e.getMessage());
                }
                break;
            default:
                break;
        }
    }
}
