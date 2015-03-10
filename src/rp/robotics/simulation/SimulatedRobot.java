package rp.robotics.simulation;

import rp.robotics.LocalisedRangeScanner;
import rp.robotics.RangeReadingsFilter;

import lejos.robotics.RangeFinder;
import lejos.robotics.RangeReadings;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;

/**
 * A simple simulated robot for testing.
 *
 * @author nah
 */
public class SimulatedRobot implements LocalisedRangeScanner {

	private Pose m_pose;
	private final LineMap m_map;
	private RangeReadings m_readings;
	private final float[] m_readingAngles;
	private boolean m_needReadings = true;
	private final float m_sensorMinRange;
	private final float m_sensorMaxRange;
	private final float m_sensorOutOfRange;
	private RangeReadingsFilter m_rangeFilter;

	/**
	 * Creates a simulated robot with a single, forward pointing sensor with a min sensor range of 7, max of 160, +/-3, and out of range value of 255. This represents the approximate reliable capabilities of the nxt ultrasonic sensor.
	 *
	 * @param _pose The initial pose of the robot
	 * @param _map The map on which the robot exists
	 */
	public static SimulatedRobot createSingleSensorRobot(Pose _pose, LineMap _map) {
		return new SimulatedRobot(_pose, _map, new float[] { 0 }, 7, 160, 255, new GaussianNoise(3, 7, 160, 255));
	}

	/**
	 * Creates a simulated robot with a single, forward pointing sensor with a min sensor range of 7, max of 160, +/-0, and out of range value of 255. This represents the approximate reliable capabilities of the nxt ultrasonic sensor.
	 *
	 * @param _pose The initial pose of the robot
	 * @param _map The map on which the robot exists
	 */
	public static SimulatedRobot createSingleNoiseFreeSensorRobot(Pose _pose, LineMap _map) {
		return new SimulatedRobot(_pose, _map, new float[] { 0 }, 7, 160, 255, null);
	}

	/**
	 * Creates a simulated robot with a single, forward pointing sensor with a min sensor range of 7, max of 160 and out of range value of 255. This represents the approximate reliable capabilities of the nxt ultrasonic sensor.
	 *
	 * @param _pose The initial pose of the robot
	 * @param _map The map on which the robot exists
	 */
	public static SimulatedRobot createSensorlessRobot(Pose _pose, LineMap _map) {
		return new SimulatedRobot(_pose, _map, new float[] { }, 0, 0, 0, null);
	}

	/**
	 * Creates a robot with no distance sensor.
	 *
	 * @param _pose
	 * @param _map
	 */
	public SimulatedRobot(Pose _pose, LineMap _map) {
		this(_pose, _map, new float[] { }, 0, 0, 0, null);
	}

	/**
	 * @param _pose The initial pose of the robot
	 * @param _map The map on which the robot exists
	 * @param _readingAngles The angles to take readings from, relative to 0 for this robot
	 * @param _sensorMaxRange Minimum sensor reading to return
	 * @param _sensorMinRange Maximum sensor reading to return
	 * @param _sensorOfOutRange The value to return if the simulated sensor would fall outside the max or min values.
	 */
	public SimulatedRobot(Pose _pose, LineMap _map, float[] _readingAngles, float _sensorMinRange, float _sensorMaxRange, float _sensorOfOutRange, RangeReadingsFilter _filter) {
		m_pose = _pose;
		m_map = _map;
		m_readings = new RangeReadings(_readingAngles.length);
		m_readingAngles = _readingAngles;
		m_sensorMaxRange = _sensorMaxRange;
		m_sensorMinRange = _sensorMinRange;
		m_sensorOutOfRange = _sensorOfOutRange;
		m_rangeFilter = _filter;
	}

	@Override
	public Pose getPose() {
		return m_pose;
	}

	@Override
	public void setPose(Pose _pose) {
		m_pose = _pose;
	}

	/**
	 * Obtain range readings from the map at the predefined angles relatve to the robot
	 */
	private void takeReadings() {

		// the pose to use for taking range readings
		Pose readingPose = new Pose(m_pose.getX(), m_pose.getY(), m_pose.getHeading());

		for (int i = 0; i < m_readingAngles.length; i++) {

			// rotate the reading pose to the angle of the sensor
			readingPose.setHeading(m_pose.getHeading() + m_readingAngles[i]);

			// and take a reading from there
			float mapRange = m_map.range(readingPose);

			// System.out.println(mapRange);
			// System.out.println(m_sensorMaxRange);
			// System.out.println(m_sensorMinRange);

			// bound reading to configured parameters
			if (mapRange > m_sensorMaxRange)
				mapRange = m_sensorOutOfRange;
			else if (mapRange < m_sensorMinRange)
				mapRange = m_sensorMinRange;

			m_readings.setRange(i, m_readingAngles[i], mapRange);

			// System.out.println(mapRange);

		}

		if (m_rangeFilter != null)
			m_readings = m_rangeFilter.apply(m_readings);

		m_needReadings = false;
	}

	@Override
	public RangeReadings getRangeValues() {
		if (m_needReadings)
			takeReadings();
		return m_readings;
	}

	@Override
	public void setAngles(float[] _angles) {
		// does nothing as these are fixed in constructor
	}

	@Override
	public RangeFinder getRangeFinder() {
		// returns null as there is no actual reading
		return null;
	}

	/***
	 * Rotate the robot by this angle.
	 *
	 * @param _angle
	 */
	public void rotate(int _angle) {
		m_pose.rotateUpdate(_angle);
		m_needReadings = true;
	}

	/***
	 * Move the robot forward by this amount. Currently does not collision checking.
	 *
	 * @param _junctionSeparation
	 */
	public void translate(float _distance) {
		m_pose.moveUpdate(_distance);
		m_needReadings = true;
	}
}
