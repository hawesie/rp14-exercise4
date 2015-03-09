package rp.robotics.simulation;

import rp.robotics.RangeReadingsFilter;

import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadings;

import java.util.Random;

/***
 * Filter to apply Gaussian noise to range readings.
 *
 * @author Nick Hawes
 */
public class GaussianNoise implements RangeReadingsFilter {

	private final double m_stdDev;
	private final float m_min;
	private final float m_max;
	private final float m_outOfRange;
	private final Random m_rand = new Random();

	/***
	 * Create a noise model with the given stddev a min of 0 and a max of 255.
	 *
	 * @param _stdDev The standard deviation of the Gaussian to apply. A value of 3 means the distribution will be centred on the the end of the range reading and apply 68% of the noise within +/- 3 and 95% of the noise +/- 6.
	 */
	public GaussianNoise(double _stdDev) {
		this(_stdDev, 7, 160, 255);
	}

	public GaussianNoise(double _stdDev, float _min, float _max, float _outOfRange) {
		m_stdDev = _stdDev;
		m_min = _min;
		m_max = _max;
		m_outOfRange = _outOfRange;
	}

	@Override
	public RangeReadings apply(RangeReadings _in) {
		RangeReadings out = new RangeReadings(_in.getNumReadings());

		for (int i = 0; i < _in.getNumReadings(); i++) {
			RangeReading reading = _in.get(i);
			out.set(i, new RangeReading(reading.getAngle(), applyNoise(reading.getRange())));
		}

		return out;
	}

	private float applyNoise(float _range) {

		float out = _range;
		if (_range != m_outOfRange) {
			out = _range + (float) (m_stdDev * m_rand.nextGaussian());
			if (out > m_max)
				out = m_max;
			else if (out < m_min)
				out = m_min;
		}
		// System.out.println(_range + " to " + out);
		return out;
	}
}
