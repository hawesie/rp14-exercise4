package rp.robotics.visualisation;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/***
 * Convenience class to kill Java when a window is closed.
 *
 * @author nah
 */
public class KillMeNow implements WindowListener {

	@Override
	public void windowActivated(WindowEvent _arg0) {

	}

	@Override
	public void windowClosed(WindowEvent _arg0) {
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent _arg0) {

	}

	@Override
	public void windowDeactivated(WindowEvent _arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent _arg0) {

	}

	@Override
	public void windowIconified(WindowEvent _arg0) {

	}

	@Override
	public void windowOpened(WindowEvent _arg0) {

	}

}
