package view.itf;

import model.AObject;

/**
 * Abstract class for table actions.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public abstract class TableAction implements Runnable {

	private AObject rowObject;
	String icon;
	String text;

	public TableAction(String icon, String text) {
		this.icon = icon;
		this.text = text;
	}

	public AObject getRowObject() {
		return rowObject;
	}

	public void setRowObject(AObject rowObject) {
		this.rowObject = rowObject;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
