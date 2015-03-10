package rp.robotics.localisation;

import rp.robotics.mapping.Heading;

import lejos.robotics.RangeReadings;

/**
 * An interface for a sensor model to reason about positions on a grid map. This interface ignores rotation actions as a simplification, and also simplifies by assuming the heading of the robot is known when the sensor readings are taken.
 *
 * @author Nick Hawes
 */
public interface SensorModel {

	/***
	 * Update the given distribution
	 *
	 * @param _dist The distribution over robot positions when the range readings were taken
	 * @param _heading The heading of the robot when the range readings were taken.
	 * @param _readings The range readings to use for the update
	 * @return
	 */
	public GridPositionDistribution updateAfterSensing(GridPositionDistribution _dist, Heading _heading, RangeReadings _readings);

}
