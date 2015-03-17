package rp.robotics.mapping;

import search.Coordinate;

public enum Heading {
	UP(0), RIGHT(1), DOWN(2), LEFT(3);

	private final byte val;

	private Heading(int val) {
		this.val = (byte) val;
	}

	public static Heading getRelativeHeading(int x, int y, Heading heading) {
		return getCompass(getHeading(x, y).val - heading.val);
	}

	public Coordinate toCoordinate() {
		switch (val) {
			case 0:
				return new Coordinate(0, 1);
			case 1:
				return new Coordinate(1, 0);
			case 2:
				return new Coordinate(0, -1);
			case 3:
				return new Coordinate(-1, 0);
			default:
				throw new IllegalArgumentException("Whut just happened?!"); // Should never get to here
		}
	}
	@SuppressWarnings("incomplete-switch")
	public static Heading getHeading(int x, int y) {
		if (x != 0)
			switch ((short) Math.signum(x)) {
				case -1:
					return LEFT;
				case 1:
					return RIGHT;
			}
		else if (y != 0)
			switch ((short) Math.signum(y)) {
				case -1:
					return DOWN;
				case 1:
					return UP;
			}

		throw new IllegalArgumentException("X or Y must be 0");
	}

	public static Heading getCompass(int heading) {
		heading += 4;    // This fixes negative numbers giving unexpected values with modulo
		switch (heading % 4) {
			case 0:
				return UP;
			case 1:
				return RIGHT;
			case 2:
				return DOWN;
			case 3:
				return LEFT;
			default:
				throw new IllegalArgumentException("No heading");
		}
	}

	public Heading getHeadingFrom(Coordinate a, Coordinate b) {
		return getRelativeHeading(a.getDelta(b));
	}

	public Heading getRelativeHeading(Coordinate delta) {
		return getRelativeHeading(delta.getX(), delta.getY(), this);
	}

	// Returns the number of degrees to turn to face the target
	public int toDegrees() {
		if (val < 2)
			return val * 90;
		else
			return (val - 4) * 90;
	}

	@Override
	// Useful for debugging, this returns the names of directions for each case
	public String toString() {
		switch (val) {
			case 0:
				return "Up";
			case 1:
				return "Right";
			case 2:
				return "Down";
			case 3:
				return "Left";
			default:
				return "No Direction";
		}
	}

	public Heading add(Heading heading) {
		return getCompass(heading.val + val);
	}
}
