package rp.robotics.mapping;

/**
 * Enumeration for discrete headings that the robot might want to represent.
 *
 * @author nah
 */
public enum Heading {
	// Heading along the positive x axis
	PLUS_X,
	// Heading along the positive y axis
	PLUS_Y,
	// Heading along the negative x axis
	MINUS_X,
	// Heading along the negative y axis
	MINUS_Y;

	/**
	 * Gets the degree orientation for a given enum value.
	 *
	 * @param _heading
	 * @return
	 */
	public static float toDegrees(Heading _heading) {
		float heading = 0;

		if (_heading == Heading.PLUS_X)
			heading = 0;
		else if (_heading == Heading.PLUS_Y)
			heading = 90;
		else if (_heading == Heading.MINUS_X)
			heading = 180;
		else if (_heading == Heading.MINUS_Y)
			heading = -90;
		else
			assert false : "Unknown value for enumeration";
		return heading;

	}

}