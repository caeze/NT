package console;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import view.img.ImageStore;
import view.l10n.L10n;
import view.util.GenericDialog;
import view.util.LabelUtil;

/**
 * Console window.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StdOutErrSwingConsole extends JFrame {

	private PrintOutErrStream printOutStream;
	private JScrollPane consoleScrollPane;
	private JTextArea consoleTextArea;
	private JToggleButton toggleScrollToBottomButton;

	private static StdOutErrSwingConsole instance;

	private StdOutErrSwingConsole(String name) {
		super(name);
		init();
		printOutStream = new PrintOutErrStream(this.consoleTextArea);
		System.setErr(new PrintStream(printOutStream, true));
		System.setOut(new PrintStream(printOutStream, true));
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
		setBounds(20, 20, 900, 600);
		System.out.println("Console started!\n================");
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @param name the window name
	 *
	 * @return an instance
	 */
	public static StdOutErrSwingConsole getInstance(String name) {
		if (instance == null) {
			instance = new StdOutErrSwingConsole(name);
		}
		return instance;
	}

	private void init() {
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel content = new JPanel(new GridBagLayout());

		consoleTextArea = new JTextArea();
		consoleTextArea.setColumns(20);
		consoleTextArea.setRows(5);
		Font font = new Font("Monospaced", consoleTextArea.getFont().getStyle(), consoleTextArea.getFont().getSize());
		consoleTextArea.setFont(font);
		consoleTextArea.setEditable(false);

		consoleScrollPane = new JScrollPane(consoleTextArea);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.99;
		constraints.weightx = 1;
		constraints.gridwidth = 2;
		content.add(consoleScrollPane, constraints);

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printOutStream.clear();
			}
		});

		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.01;
		constraints.weightx = 0.6;
		content.add(clearButton, constraints);

		toggleScrollToBottomButton = new JToggleButton("Scroll to bottom");
		toggleScrollToBottomButton.setSelected(true);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.01;
		constraints.weightx = 0.4;
		content.add(toggleScrollToBottomButton, constraints);

		getContentPane().add(content);

		setSize(900, 500);
		setLocation(50, 50);

		try {
			ImageIcon icon = ImageStore.getImageIcon("logo.png");
			setIconImage(icon.getImage());
		} catch (Exception e) {
			Log.error(StdOutErrSwingConsole.class, e.getMessage());
		}
	}

	private class PrintOutErrStream extends ByteArrayOutputStream {

		final int maxTextAreaSize = 1000000;
		private JTextArea textArea;

		public PrintOutErrStream(JTextArea textArea) {
			this.textArea = textArea;
		}

		public void clear() {
			textArea.setText("");
		}

		public void flush() throws IOException {
			synchronized (this) {
				super.flush();
				String outputStr = this.toString();
				super.reset();
				if (textArea.getText().length() > maxTextAreaSize) {
					textArea.replaceRange("", 0, 1000);
				}
				textArea.append(outputStr);
				if (outputStr.toLowerCase().contains("exception") || outputStr.toLowerCase().contains("error")) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							new GenericDialog(L10n.getString("error"), Arrays.asList(new JLabel(LabelUtil.styleLabel(outputStr)))).show();
						}
					});
				}
				if (toggleScrollToBottomButton.isSelected()) {
					JScrollBar vertical = consoleScrollPane.getVerticalScrollBar();
					vertical.setValue(vertical.getMaximum() + 500);
				}
			}
		}
	}
}
