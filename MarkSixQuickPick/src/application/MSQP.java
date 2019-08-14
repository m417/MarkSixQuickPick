package application;

import java.net.URL;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.MSQPcontroller;
import model.MSQPmodel;
import view.MSQPview;

public class MSQP {

	private MSQP() {
	}

	public static void main(String[] args) {
		if (getOS().startsWith("mac")) {
			System.setProperty("apple.awt.application.name", "Mark Six Quick Pick");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		MSQPmodel model = new MSQPmodel(1, 49);
		MSQPview view = new MSQPview();
		MSQPcontroller controller = new MSQPcontroller(model, view);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				controller.showView();
			}
		});
	}

	public static String getOS() {
		return System.getProperty("os.name").toLowerCase();
	}

	public static double getJavaVersion() {
		return Double.parseDouble(System.getProperty("java.specification.version"));
	}

	public static URL getResource(String theName) {
		return MSQP.class.getResource(theName);
	}

}
