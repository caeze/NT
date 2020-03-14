package control;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;

import console.Log;
import console.StdOutErrSwingConsole;
import filehandling.FileHandlingUtil;
import model.Model;
import model.Project;
import nt.NT;
import preferences.Preferences;
import view.StartMenu;
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
		View.getInstance().pushViewComponent(new StartMenu());
	}

	public boolean saveCurrentProject() {
		String path = FileHandlingUtil.getInstance().showSaveFileSelector("json");
		if (path != null) {
			if (!path.endsWith(".json")) {
				path = path + ".json";
			}
			Preferences.getInstance().lastOpenedProjectPath = path;
			Preferences.getInstance().persist();
			String content = Model.getInstance().getCurrentProject().toJsonString();
			FileHandlingUtil.getInstance().writeStringToFile(path, content);
			return true;
		}
		return false;
	}

	public boolean loadLastOpenedProject() {
		if (!lastOpenedProjectExists()) {
			Log.error(Control.class, "Can not load last project as it does not exist!");
			return false;
		}

		if (Model.getInstance().hasUnsavedChanges()) {
			List<String> options = new ArrayList<>();
			options.add(L10n.getString("loadProjectWithoutSavingCurrentOne"));
			options.add(L10n.getString("save"));
			options.add(L10n.getString("cancel"));
			GenericDialog dialog = new GenericDialog(L10n.getString("reallyLoadProjectWithoutSavingCurrentOne"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("unsavedChangesWillBeLost")))), options);
			int selection = dialog.show();
			switch (selection) {
			case 0:
				loadProjectFromDisk(Preferences.getInstance().lastOpenedProjectPath);
				return true;
			case 1:
				boolean success = saveCurrentProject();
				if (success) {
					loadProjectFromDisk(Preferences.getInstance().lastOpenedProjectPath);
					return true;
				} else {
					GenericDialog failedDialog = new GenericDialog(L10n.getString("saveFailed"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("saveFailed")))), true);
					failedDialog.show();
				}
				break;
			case 2:
			default:
				break;
			}
		} else {
			loadProjectFromDisk(Preferences.getInstance().lastOpenedProjectPath);
			return true;
		}
		return false;
	}

	public boolean loadProjectFromDisk() {
		if (Model.getInstance().hasUnsavedChanges()) {
			List<String> options = new ArrayList<>();
			options.add(L10n.getString("loadProjectWithoutSavingCurrentOne"));
			options.add(L10n.getString("save"));
			options.add(L10n.getString("cancel"));
			GenericDialog dialog = new GenericDialog(L10n.getString("reallyLoadProjectWithoutSavingCurrentOne"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("unsavedChangesWillBeLost")))), options);
			int selection = dialog.show();
			switch (selection) {
			case 0:
				File f = FileHandlingUtil.getInstance().showOpenFileSelector("json");
				if (f != null) {
					loadProjectFromDisk(f.getAbsolutePath());
					return true;
				}
				break;
			case 1:
				boolean success = saveCurrentProject();
				if (success) {
					f = FileHandlingUtil.getInstance().showOpenFileSelector("json");
					if (f != null) {
						loadProjectFromDisk(f.getAbsolutePath());
						return true;
					}
				} else {
					GenericDialog failedDialog = new GenericDialog(L10n.getString("saveFailed"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("saveFailed")))), true);
					failedDialog.show();
				}
				break;
			case 2:
			default:
				break;
			}
		} else {
			File f = FileHandlingUtil.getInstance().showOpenFileSelector("json");
			if (f != null) {
				loadProjectFromDisk(f.getAbsolutePath());
				return true;
			}
		}
		return false;
	}

	public void loadProjectFromDisk(String projectPath) {
		String projectJsonString = FileHandlingUtil.getInstance().readFileAsString(projectPath);
		Project project = (Project) new Project().fillFromJsonString(projectJsonString);
		Model.getInstance().setCurrentProject(project);
	}

	public boolean loadEmptyProject() {
		if (Model.getInstance().hasUnsavedChanges()) {
			List<String> options = new ArrayList<>();
			options.add(L10n.getString("loadProjectWithoutSavingCurrentOne"));
			options.add(L10n.getString("save"));
			options.add(L10n.getString("cancel"));
			GenericDialog dialog = new GenericDialog(L10n.getString("reallyLoadProjectWithoutSavingCurrentOne"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("unsavedChangesWillBeLost")))), options);
			int selection = dialog.show();
			switch (selection) {
			case 0:
				Model.getInstance().loadEmptyProject();
				return true;
			case 1:
				boolean success = saveCurrentProject();
				if (success) {
					Model.getInstance().loadEmptyProject();
					return true;
				} else {
					GenericDialog failedDialog = new GenericDialog(L10n.getString("saveFailed"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("saveFailed")))), true);
					failedDialog.show();
				}
				break;
			case 2:
			default:
				break;
			}
		} else {
			Model.getInstance().loadEmptyProject();
			return true;
		}
		return false;
	}

	public boolean lastOpenedProjectExists() {
		if (Preferences.getInstance().lastOpenedProjectPath.isEmpty()) {
			return false;
		}
		File lastOpenedProjectFile = new File(Preferences.getInstance().lastOpenedProjectPath);
		return lastOpenedProjectFile.exists();
	}

	public void exitProgram() {
		if (Model.getInstance().hasUnsavedChanges()) {
			List<String> options = new ArrayList<>();
			options.add(L10n.getString("leaveNT"));
			options.add(L10n.getString("save"));
			options.add(L10n.getString("cancel"));
			GenericDialog dialog = new GenericDialog(L10n.getString("reallyQuit"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("unsavedChangesWillBeLost")))), options);
			int selection = dialog.show();
			LoadingAnimation.killLoadingAnim();
			switch (selection) {
			case 0:
				System.exit(0);
				break;
			case 1:
				boolean success = saveCurrentProject();
				if (success) {
					System.exit(0);
				} else {
					GenericDialog failedDialog = new GenericDialog(L10n.getString("saveFailed"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("saveFailed")))), true);
					failedDialog.show();
				}
				break;
			case 2:
			default:
				break;
			}
		} else {
			GenericDialog dialog = new GenericDialog(L10n.getString("reallyQuit"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("reallyQuitQuestion")))));
			int selection = dialog.show();
			if (selection == GenericDialog.SELECTION_OK) {
				LoadingAnimation.killLoadingAnim();
				System.exit(0);
			}
		}
	}
}
