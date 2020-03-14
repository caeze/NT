package view.itf;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Interface for view components.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public interface IViewComponent {

	public enum Result {
		NONE, SAVE, CANCEL;
	}

	public List<JButton> getButtonsLeft();

	public List<JComponent> getComponentsCenter();

	public List<JButton> getButtonsRight();

	public JComponent initializeViewComponent(boolean firstInitialization);

	public void uninitializeViewComponent();

	public void resultFromLastViewComponent(IViewComponent component, Result result);
}
