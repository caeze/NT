package control;

import console.Log;
import console.StdOutErrSwingConsole;
import filehandling.FileHandlingUtil;
import filehandling.HttpFileUtil;
import nt.NT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import model.Project;
import model.ProjectJsonConverter;
import preferences.Preferences;
import view.ClassEditor;
import view.MainMenu;
import view.View;
import view.l10n.L10n;
import view.util.GenericDialog;
import view.util.LabelUtil;
import view.util.LoadingAnimation;

/**
 * Control of the MVC pattern.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Control {

	private static Control instance;

	private Control() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static Control getInstance() {
		if (instance == null) {
			instance = new Control();
		}
		return instance;
	}

	public void init() {
		if (NT.IS_DEBUG) {
			Preferences.getInstance().logLevel = Log.LOG_LEVEL_DEBUG;
			Preferences.getInstance().showConsole = true;
		}
		if (Preferences.getInstance().showConsole) {
			StdOutErrSwingConsole.getInstance(L10n.getString("NT"));
		}
		View.getInstance().setContent(MainMenu.getInstance());
	}

	public void exitProgram() {
		System.exit(0);
//        if (ClassEditor.getInstance().editorHasChanges()) {
//            List<String> options = new ArrayList<>();
//            options.add(L10n.getString("leaveNT"));
//            options.add(L10n.getString("save"));
//            options.add(L10n.getString("cancel"));
//            GenericDialog dialog = new GenericDialog(L10n.getString("reallyQuit"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("unsavedChangesWillBeLost")))), options);
//            int selection = dialog.show();
//            LoadingAnimation.killLoadingAnim();
//            switch (selection) {
//                case 0:
//                    System.exit(0);
//                    break;
//                case 1:
//                    boolean success = saveCurrentProject();
//                    if (success) {
//                        System.exit(0);
//                    }
//                    break;
//                case 2:
//                default:
//                    break;
//            }
//        } else {
//            GenericDialog dialog = new GenericDialog(L10n.getString("reallyQuit"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("reallyQuitQuestion")))));
//            int selection = dialog.show();
//            if (selection == GenericDialog.SELECTION_OK) {
//                LoadingAnimation.killLoadingAnim();
//                System.exit(0);
//            }
//        }
	}

	public boolean saveCurrentProject() {
//        String path = FileHandlingUtil.getInstance().showSaveFileSelector();
//        if (path != null) {
//            if (!path.endsWith(".japy")) {
//                path = path + ".japy";
//            }
//            Project project = new Project(ClassEditor.getInstance().getEditorTitles(), ClassEditor.getInstance().getEditorContents(), NT.VERSION, new Date());
//            String content = ProjectJsonConverter.getInstance().projectToJsonString(project);
//            FileHandlingUtil.getInstance().writeStringToFile(path, content);
//            return true;
//        }
		return false;
	}

//
	public void loadEmptyProject() {
//        List<String> titles = new ArrayList<>();
//        titles.add(L10n.getString("new") + "_1");
//        List<String> files = new ArrayList<>();
//        files.add("");
//        ClassEditor.getInstance().setEditorContents(titles, files);
	}

//
	public void loadProjectFromDisk() {
//        if (ClassEditor.getInstance().editorHasChanges()) {
//            List<String> options = new ArrayList<>();
//            options.add(L10n.getString("loadProjectWithoutSavingCurrentOne"));
//            options.add(L10n.getString("save"));
//            options.add(L10n.getString("cancel"));
//            GenericDialog dialog = new GenericDialog(L10n.getString("reallyLoadProjectWithoutSavingCurrentOne"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("unsavedChangesWillBeLost")))), options);
//            int selection = dialog.show();
//            LoadingAnimation.killLoadingAnim();
//            switch (selection) {
//                case 0:
//                    ClassEditor.getInstance().clearEditors();
//                    String path = FileHandlingUtil.getInstance().showOpenFileSelector();
//                    if (path != null) {
//                        String content = FileHandlingUtil.getInstance().readFileAsString(path);
//                        Project project = ProjectJsonConverter.getInstance().jsonStringToProject(content);
//                        ClassEditor.getInstance().setEditorContents(project.getTitles(), project.getFiles());
//                    }
//                    break;
//                case 1:
//                    boolean success = saveCurrentProject();
//                    if (success) {
//                        ClassEditor.getInstance().clearEditors();
//                        String pathToLoad = FileHandlingUtil.getInstance().showOpenFileSelector();
//                        if (pathToLoad != null) {
//                            String content = FileHandlingUtil.getInstance().readFileAsString(pathToLoad);
//                            Project project = ProjectJsonConverter.getInstance().jsonStringToProject(content);
//                            ClassEditor.getInstance().setEditorContents(project.getTitles(), project.getFiles());
//                        }
//                        break;
//                    }
//                    break;
//                case 2:
//                default:
//                    break;
//            }
//        } else {
//            ClassEditor.getInstance().clearEditors();
//            String path = FileHandlingUtil.getInstance().showOpenFileSelector();
//            if (path != null) {
//                String content = FileHandlingUtil.getInstance().readFileAsString(path);
//                Project project = ProjectJsonConverter.getInstance().jsonStringToProject(content);
//                ClassEditor.getInstance().setEditorContents(project.getTitles(), project.getFiles());
//            }
//        }
	}

//
	public void loadProjectFromNetwork() {
//        String path = Preferences.getInstance().projectLocation;
//        LoadingAnimation.showLoadingAnim();
//        HttpFileUtil.getInstance().getFileViaHttp(path, new HttpFileUtil.IHttpCallback() {
//            @Override
//            public void success(String data) {
//                Project project = ProjectJsonConverter.getInstance().jsonStringToProject(data);
//                ClassEditor.getInstance().setEditorContents(project.getTitles(), project.getFiles());
//                LoadingAnimation.killLoadingAnim();
//            }
//
//            @Override
//            public void failure(String data) {
//                Log.error(Control.class, "Could not load project from network: " + data);
//                LoadingAnimation.killLoadingAnim();
//            }
//        });
	}
}
