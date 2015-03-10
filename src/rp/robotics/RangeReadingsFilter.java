package rp.robotics;

import lejos.robotics.RangeReadings;

/***
 * An interface to represent the application of a filter to a list of range readings.
 *
 * @author Nick Hawes
 */
public interface RangeReadingsFilter {

	/***
	 * Apply the filter
	 *
	 * @param _in The range readings to filter
	 * @return The resulting range readings
	 */
	RangeReadings apply(RangeReadings _in);

}
