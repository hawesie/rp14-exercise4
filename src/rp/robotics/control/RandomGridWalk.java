package rp.robotics.control;

import rp.robotics.simulation.SimulatedRobot;

import lejos.util.Delay;

import java.util.Random;

/***
 * Moves a simulated robot around on a grid map in a random way. Assumes that any time a distance of junctionSeparation is free it can move that far in that direction, else it turns 90 degrees until it can.
 *
 * @author nah
 */
public class RandomGridWalk implements Runnable {

	// This would be a great place to introduce an abstraction...
	private final SimulatedRobot m_robot;
	private final float m_junctionSeparation;
	private boolean m_run = true;
	private final long m_actionDelay;
	private final Random m_rand;

	/***
	 * Create a random walk that acts every second.
	 *
	 * @param _robot The simulated robot
	 * @param _junctionSeparation How far about grid positions are
	 * @param _actionDelay How long to wait between actions.
	 */

	public RandomGridWalk(SimulatedRobot _robot, float _junctionSeparation, long _actionDelay) {

		if (_robot.getRangeValues().getRange(0f) == -1f)
			throw new IllegalArgumentException("Robot must have a forward-pointing sensor to use this code");

		m_robot = _robot;
		m_junctionSeparation = _junctionSeparation;
		m_actionDelay = _actionDelay;
		m_rand = new Random();
	}

	/***
	 * Create a random walk that acts every second.
	 *
	 * @param _robot The simulated robot
	 * @param _junctionSeparation How far about grid positions are
	 */
	public RandomGridWalk(SimulatedRobot _robot, float _junctionSeparation) {
		this(_robot, _junctionSeparation, 1000);
	}

	@Override
	public void run() {
		while (m_run) {

			// get the range at relative angle 0 to the robot, i.e. straight
			// in front
			float rangeInFront = m_robot.getRangeValues().getRange(0f);
			if (rangeInFront > m_junctionSeparation + 5)
				m_robot.translate(m_junctionSeparation);
			else if (m_rand.nextBoolean())
				m_robot.rotate(90);
			else
				m_robot.rotate(-90);

			Delay.msDelay(m_actionDelay);

		}

	}
}
