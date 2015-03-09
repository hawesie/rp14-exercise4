package rp.robotics.visualisation;

import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.RPLineMap;

import javax.swing.JFrame;

public class LineMapViewer {

	public LineMapVisualisation run() {

		// Create a frame to contain the viewer
		JFrame frame = new JFrame("Map Viewer");

		// Get the line map to display
		RPLineMap lineMap = MapUtils.create2014Map2();

		// Create the visualisation of this map with 2 pixels as 1 cm
		LineMapVisualisation mapVis = new LineMapVisualisation(lineMap, 2, true);

		// Add visualisation to frame
		frame.add(mapVis);
		frame.addWindowListener(new KillMeNow());

		frame.pack();
		frame.setSize(800, 600);
		frame.setVisible(true);

		return mapVis;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LineMapViewer demo = new LineMapViewer();
		demo.run();
	}
}
