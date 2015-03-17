package rp.robotics.visualisation;

import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.mapping.NicksGridMap;

import lejos.robotics.mapping.LineMap;

import javax.swing.JFrame;

public class GridMapViewer {
	public void run(LineMap lineMap, IGridMap gridMap, int width, int height) {
		GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, lineMap, 2, true);

		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());
		frame.add(mapVis);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		GridMapViewer demo = new GridMapViewer();
		LineMap lineMap = MapUtils.create2015Map1();
		demo.run(lineMap, new NicksGridMap(12, 8, 15, 15, 30, lineMap), 820, 600);
	}
}
