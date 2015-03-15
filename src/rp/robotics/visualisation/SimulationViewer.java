package rp.robotics.visualisation;

import rp.robotics.control.RandomGridWalk;
import rp.robotics.simulation.SimulatedRobot;

import lejos.robotics.navigation.Pose;

/**
 * Demonstrates the use, and visualisation, of a simulated simple robot on a line map;
 *
 * @author Nick Hawes
 */
public class SimulationViewer {

	public void run() {

		// Create a display for the world
		LineMapViewer lmv = new LineMapViewer();
		LineMapVisualisation mapViz = lmv.run();

		// Pretending we have a grid map....

		// how far apart are the junctions on the grid
		float junctionSeparation = 30;

		// where is the first point relative to the boundary of the map
		int xInset = 15;
		int yInset = 15;

		// Create a simulated robot with a single, forward-pointing sensor
		SimulatedRobot robot = SimulatedRobot.createSingleSensorRobot(new Pose(xInset, yInset, 0), mapViz.getLineMap());

		// Add it to the visualisation
		mapViz.addRobot(robot);

		// Create a behaviour to drive it around
		RandomGridWalk walk = new RandomGridWalk(robot, junctionSeparation);
		walk.run();

	}

	public static void main(String[] args) {
		SimulationViewer demo = new SimulationViewer();
		demo.run();
	}

}
