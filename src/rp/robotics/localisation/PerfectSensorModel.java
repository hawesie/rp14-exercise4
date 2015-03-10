/**
 *
 */
package rp.robotics.localisation;

import rp.robotics.mapping.Heading;

import lejos.robotics.RangeReadings;

/**
 * Empty sensor model for testing.
 *
 * @author Nick Hawes
 */
public class PerfectSensorModel implements SensorModel {

	/*
	 * (non-Javadoc)
	 *
	 * @see rp.robotics.localisation.SensorModel#updateAfterSensing(rp.robotics.
	 * localisation.GridPositionDistribution, rp.robotics.mapping.Heading,
	 * lejos.robotics.RangeReadings)
	 */
	@Override
	public GridPositionDistribution updateAfterSensing(GridPositionDistribution _dist, Heading _heading, RangeReadings _readings) {

		return _dist;
	}

}
