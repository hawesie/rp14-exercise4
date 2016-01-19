package rp.robotics.simulation;

import java.util.function.Predicate;

import rp.util.Rate;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * This class simulates the desired behaviour of a regulated motor. It does not
 * simulate noise or errors.
 * 
 * @author Nick Hawes
 *
 */
public class SimulatedMotor implements RegulatedMotor {

	// target speed of motor in degrees/second
	protected double m_targetSpeed = 360;
	// acceleration of motor in degrees/second/second
	protected int m_acceleration = 6000;
	// current speed of motor in degrees/second
	protected double m_commandedSpeed = 0;
	protected double m_measuredSpeed = 0;

	// tacho count of motor in degrees
	protected double m_tachoCount = 0;

	// whether or not the motor should be moving
	private boolean m_isMoving = false;

	// listener for motor actions, only support a single listener to match NXT
	// implementation
	private RegulatedMotorListener m_listener = null;

	private enum MotorState {
		ACCELERATING, REGULATING, STOPPED
	};

	private MotorState m_state = MotorState.STOPPED;

	private final double FORWARD = 1f;
	private final double STOPPED = 0f;
	private final double BACKWARD = -1f;

	private double m_direction = STOPPED;
	private Thread m_moveThread;
	private int m_limitAngle;
	private Thread m_regulateThread;

	private void notifyListener(boolean _started) {
		if (m_listener != null) {
			if (_started) {
				m_listener.rotationStarted(this, getTachoCount(), isStalled(),
						System.currentTimeMillis());
			} else {
				m_listener.rotationStopped(this, getTachoCount(), isStalled(),
						System.currentTimeMillis());
			}
		}

	}

	/**
	 * Regulate the speed of the motor relative to the target.
	 */
	private void regulate() {

		double cycleTimeSecs = 0.8;
		Rate rate = new Rate(1 / cycleTimeSecs);
		double lastReading = getTachoCount(), currentReading, difference;

		while (m_isMoving) {
			currentReading = getTachoCount();
			difference = Math.abs(currentReading - lastReading);
			m_measuredSpeed = difference / cycleTimeSecs;
			difference = m_targetSpeed - m_measuredSpeed;
			// System.out.println("spped diff: " + difference);
			// System.out.println("measured: " + m_measuredSpeed);

			if (m_state == MotorState.REGULATING) {
				m_commandedSpeed = m_commandedSpeed + 1
						* (cycleTimeSecs * difference);
			}

			lastReading = currentReading;

			rate.sleep();

		}

		m_measuredSpeed = 0;
	}

	/**
	 * Move until stopped or until the predicate tests true.
	 * 
	 * @param _tachoPredicate
	 */
	private void move(Predicate<Integer> _tachoPredicate) {

		// the resolution of the movement simulation

		// tacho should have a resolution of 4 degrees (+/- 2) so this loop
		// needs to increment in smaller steps, e.g. 2 degree steps

		Rate rate = new Rate(m_targetSpeed / 2d);
		double cycleTimeSecs = 2d / m_targetSpeed;

		notifyListener(true);

		m_state = MotorState.ACCELERATING;
		while (m_isMoving && !_tachoPredicate.test(getTachoCount())) {

			if (m_state == MotorState.ACCELERATING) {
				if (m_commandedSpeed < m_targetSpeed) {
					// don't accelerate past target speed
					m_commandedSpeed = Math.min(m_commandedSpeed
							+ (m_acceleration * cycleTimeSecs), m_targetSpeed);
					// System.out.println("accelerating to speed: "
					// + m_commandedSpeed);

				} else {
					m_state = MotorState.REGULATING;
				}
			}

			m_tachoCount += (cycleTimeSecs * m_commandedSpeed * m_direction);

			rate.sleep();

		}

		// need to set this if the tacho predicate stopped the move
		m_isMoving = false;
		m_commandedSpeed = 0;
		m_state = MotorState.STOPPED;
		notifyListener(false);
	}

	@Override
	protected void finalize() throws Throwable {
		// make sure any movement thread can exit on shutdown
		m_isMoving = false;
	}

	private void startMove(double _direction) {
		startMove(_direction, i -> false);
	}

	private void startMove(double _direction, Predicate<Integer> _tachoPredicate) {

		// if moving in a different direction, make sure we've stopped before
		// moving again
		if (m_isMoving) {
			if (m_direction != _direction) {
				stop(false);
			} else {
				return;
			}
		} else if (m_moveThread != null) {
			// if we're not moving, make sure the last move thread has cleaned
			// up
			waitComplete();
		}

		m_direction = _direction;
		m_isMoving = true;

		synchronized (this) {
			m_moveThread = new Thread(new Runnable() {
				@Override
				public void run() {
					move(_tachoPredicate);
	
				}
			});
			m_regulateThread = new Thread(new Runnable() {
	
				@Override
				public void run() {
					regulate();
	
				}
			});
	
			m_moveThread.setPriority(10);
			m_regulateThread.setPriority(8);
	
			m_moveThread.setDaemon(true);
			m_regulateThread.setDaemon(true);
			m_moveThread.start();
			m_regulateThread.start();
		}
	}

	@Override
	public void forward() {
		startMove(FORWARD);
	}

	@Override
	public void backward() {
		startMove(BACKWARD);
	}

	@Override
	public void stop() {
		stop(false);
	}

	@Override
	public void flt() {
		// the simulation can't float, so this is as good as we get.
		stop();
	}

	@Override
	public boolean isMoving() {
		return m_isMoving;
	}

	@Override
	public int getRotationSpeed() {
		return getSpeed();
	}

	@Override
	public int getTachoCount() {
		return (int) Math.round(Math.floor(m_tachoCount));
	}

	@Override
	public void resetTachoCount() {
		m_tachoCount = 0;
	}

	@Override
	public void addListener(RegulatedMotorListener _listener) {
		m_listener = _listener;
	}

	@Override
	public RegulatedMotorListener removeListener() {
		RegulatedMotorListener listener = m_listener;
		m_listener = null;
		return listener;
	}

	@Override
	public void stop(boolean _immediateReturn) {
		m_isMoving = false;
		if (!_immediateReturn) {
			waitComplete();
		}
		m_direction = STOPPED;
	}

	@Override
	public void flt(boolean _immediateReturn) {
		stop(_immediateReturn);
	}

	@Override
	public void waitComplete() {
		if (m_moveThread != null) {
			try {
				m_moveThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (m_regulateThread != null) {
			try {
				m_regulateThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void rotate(int _angle, boolean _immediateReturn) {
		rotateTo(getTachoCount() + _angle, _immediateReturn);
	}

	@Override
	public void rotate(int _angle) {
		rotateTo(getTachoCount() + _angle, false);
	}

	@Override
	public void rotateTo(int _limitAngle) {
		rotateTo(_limitAngle, false);

	}

	@Override
	public void rotateTo(int _limitAngle, boolean _immediateReturn) {
		m_limitAngle = _limitAngle;
		double direction = FORWARD;
		Predicate<Integer> target = i -> i >= _limitAngle - 2;
		if (_limitAngle < getTachoCount()) {
			direction = BACKWARD;
			target = i -> i <= _limitAngle + 2;
		}
		startMove(direction, target);
		if (!_immediateReturn) {
			waitComplete();
		}
	}

	@Override
	public int getLimitAngle() {
		return m_limitAngle;
	}

	@Override
	public void setSpeed(int _speed) {
		if (_speed > 0 && _speed <= getMaxSpeed()) {
			m_targetSpeed = _speed;
		} else {
			throw new IllegalArgumentException(
					"Speed must be greater than 0 and less than or equal to "
							+ getMaxSpeed());
		}

	}

	@Override
	public int getSpeed() {
		return Math.round((float) Math.floor(m_measuredSpeed));
	}

	@Override
	public float getMaxSpeed() {
		// It is generally assumed, that the maximum accurate speed of Motor is
		// 100 degree/second * Voltage
		// 9.4V seems like a high value for the voltage
		return 900;
	}

	@Override
	public boolean isStalled() {
		return false;
	}

	@Override
	public void setStallThreshold(int _error, int _time) {
	}

	@Override
	public void setAcceleration(int _acceleration) {
		if (_acceleration > 0 && _acceleration <= 6000) {
			m_acceleration = _acceleration;
		} else {
			throw new IllegalArgumentException(
					"Acceleration must be greater than 0 and less than or equal to 6000");
		}

	}

	public static void main(String[] args) {
		// SimulatedMotor motor = new SimulatedMotor();
		// motor.forward();
		// int prev = 0;
		// Rate rate = new Rate(0.5);
		// for (int i = 0; i < 20; i++) {
		// int curr = motor.getTachoCount();
		// System.out.println(motor.getSpeed() + " " + (curr - prev));
		// prev = curr;
		// rate.sleep();
		// }

		// RegulatedMotor motor = new SimulatedMotor();
		// int[] targets = { 0, 361, -33, 400, 404, -27, -666, 1024 };
		// for (int target : targets) {
		// motor.rotateTo(target, false);
		// System.out.println("Rotation for " + target + " att " +
		// motor.getTachoCount());
		// }

		DifferentialPilot dp = new DifferentialPilot(56, 163,
				new SimulatedMotor(), new SimulatedMotor());

		OdometryPoseProvider pp = new OdometryPoseProvider(dp);

		System.out.println(pp.getPose());

		double distanceMm = 500;

		dp.travel(distanceMm);
		dp.rotate(-90);
		dp.travel(distanceMm);

		System.out.println(pp.getPose());
	}

}
