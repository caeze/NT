package view;

import console.Log;
import control.Control;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonTabComponent;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.GenericDialog;
import view.util.LabelUtil;
import view.util.LoadingAnimation;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Editor implements IViewComponent {

    private JTabbedPane tabbedPane;

    private static Editor instance;

    private List<JTextArea> editors = new ArrayList<>();
    private JTextArea consoleTextArea;

    private Editor() {
        // hide constructor, singleton pattern
    }

    /**
     * Get an instance, singleton pattern.
     *
     * @return an instance
     */
    public static Editor getInstance() {
        if (instance == null) {
            instance = new Editor();
        }
        return instance;
    }

    @Override
    public List<JButton> getButtonsLeft() {
        List<JButton> retList = new ArrayList<>();
        JButton backButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                View.getInstance().setContent(MainMenu.getInstance());
            }
        }, "back.png", 40, 40, L10n.getString("back"));
        retList.add(backButton);
        JButton openButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                Control.getInstance().loadProjectFromDisk();
            }
        }, "directory.png", 40, 40, L10n.getString("open"));
        retList.add(openButton);
        JButton saveButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                Control.getInstance().saveCurrentProject();
            }
        }, "save.png", 40, 40, L10n.getString("save"));
        retList.add(saveButton);
        JButton addNewButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                JTextField nameTextField = new JTextField();
                GenericDialog dialog = new GenericDialog(L10n.getString("enterName"), Arrays.asList(nameTextField), true);
                int selection = dialog.show();
                if (selection == GenericDialog.SELECTION_OK) {
                    String fileName = nameTextField.getText();
                    addTab(fileName, "");
                }
            }
        }, "add_new.png", 40, 40, L10n.getString("addNewFile"));
        retList.add(addNewButton);
        return retList;
    }

    @Override
    public List<JComponent> getComponentsCenter() {
        List<JComponent> retList = new ArrayList<>();
        JButton icon = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Desktop.isDesktopSupported()) {
                        String s = Preferences.getInstance().projectLocation;
                        Desktop.getDesktop().browse(new URI(s.substring(0, s.lastIndexOf("/") + 1)));
                    }
                } catch (Exception e) {
                    Log.error(Editor.class, e.getMessage());
                }
            }
        }, "logo.png", 48, 48);
        icon.setSelected(true);
        retList.add(icon);
        return retList;
    }

    @Override
    public List<JButton> getButtonsRight() {
        List<JButton> retList = new ArrayList<>();
        retList.add(ButtonUtil.createButton("empty.png", 40, 40));
        JButton runButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                // TODO fix
            }
        }, "play.png", 40, 40, L10n.getString("run"));
        retList.add(runButton);
        JButton cancelButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                // TODO fix
            }
        }, "block.png", 40, 40, L10n.getString("cancelRun"));
        retList.add(cancelButton);
        JButton exitButton = ButtonUtil.createButton(new Runnable() {
            @Override
            public void run() {
                Control.getInstance().exitProgram();
            }
        }, "clear.png", 40, 40, L10n.getString("exitNT"));
        retList.add(exitButton);
        return retList;
    }

    @Override
    public JComponent initializeViewComponent() {
        // add tabs area for text editor
        tabbedPane = getEditorArea();

        // add console area
        JComponent consoleArea = getConsoleArea();

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel retComponent = new JPanel(gridBagLayout);
        retComponent.setBackground(ColorStore.GREEN);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, consoleArea);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation((int) (View.getInstance().getContentViewSize().getHeight() * 0.75));
        View.getInstance().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                splitPane.setDividerLocation((int) (View.getInstance().getContentViewSize().getHeight() * 0.75));
            }
        });
        retComponent.add(splitPane, constraints);

        return retComponent;
    }

    @Override
    public void uninitializeViewComponent() {
        instance = null;
    }

    public void addTab(String title, String fileContent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorStore.BACKGROUND_VERY_LIGHT);

        GridBagConstraints constraints = new GridBagConstraints();
        JPanel content = new JPanel(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        panel.add(content);
        tabbedPane.addTab(title, null, panel);
        tabbedPane.setTabComponentAt(editors.size() - 1, new ButtonTabComponent(tabbedPane));
        tabbedPane.setSelectedIndex(editors.size() - 1);
        tabbedPane.invalidate();
        tabbedPane.repaint();
    }

    private void removeAllTabs() {
        if (tabbedPane != null) {
            tabbedPane.removeAll();
        }
    }

    private JTabbedPane getEditorArea() {
        JTabbedPane t = new JTabbedPane();
        t.setBackground(ColorStore.RED);
        return t;
    }

    private JComponent getConsoleArea() {
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel content = new JPanel(new GridBagLayout());

        consoleTextArea = new JTextArea();
        consoleTextArea.setColumns(20);
        consoleTextArea.setRows(5);
        consoleTextArea.setEditable(false);
        consoleTextArea.setBackground(ColorStore.BACKGROUND_CONSOLE);
        consoleTextArea.setText("Console ready\n-------------");
        consoleTextArea.setCaretColor(ColorStore.FOREGROUND_CONSOLE);
        consoleTextArea.setForeground(ColorStore.FOREGROUND_CONSOLE);

        JScrollPane consoleScrollPane = new JScrollPane();
        consoleScrollPane.setViewportView(consoleTextArea);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        content.add(consoleScrollPane, constraints);

        return content;
    }

    public List<String> getEditorTitles() {
        List<String> retList = new ArrayList<>();
        for (int i = 0; i < editors.size(); i++) {
            retList.add(tabbedPane.getTitleAt(i));
        }
        return retList;
    }

    public List<String> getEditorContents() {
        List<String> retList = new ArrayList<>();
        for (JTextArea editor : editors) {
            retList.add(editor.getText());
        }
        return retList;
    }

    public void setEditorContents(List<String> titles, List<String> contents) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                removeAllTabs();
                for (int i = 0; i < titles.size(); i++) {
                    addTab(titles.get(i), contents.get(i));
                }
            }
        });
    }

    public boolean editorHasChanges() {
        //TODO fix
        return !editors.isEmpty();
    }

    public void clearEditors() {
        editors.clear();
    }

    public void closeTab(int index) {
        GenericDialog dialog = new GenericDialog(L10n.getString("reallyClose"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("reallyClose")))));
        int selection = dialog.show();
        if (selection == GenericDialog.SELECTION_OK) {
            tabbedPane.remove(index);
            editors.remove(index);
        }
    }
}
