package rp.robotics.localisation;

import rp.robotics.mapping.IGridMap;

/**
 * A class to represent a probability distribution of a robot's pose on a {@link IGridMap}. This class ignores the heading of the robot at a grid point. Feel free to extend or rewrite this class to add missing functionality.
 *
 * @author Nick Hawes
 */
public class GridPositionDistribution {

	private static final Float OBSTRUCTED_POINT = null;
	private final IGridMap m_gridMap;
	private final int m_gridHeight;
	private final int m_gridWidth;
	private final Float[][] m_grid;

	/**
	 * Initialise uniform distribution across unobstructed grid points.
	 *
	 * @param _gridMap
	 */
	public GridPositionDistribution(IGridMap _gridMap) {
		m_gridMap = _gridMap;
		m_gridWidth = _gridMap.getXSize();
		m_gridHeight = _gridMap.getYSize();

		m_grid = new Float[m_gridHeight][m_gridWidth];

		// flag obstructed points for future reference
		int obstructedPoints = 0;
		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getYSize(); y++)
				if (m_gridMap.isObstructed(x, y)) {
					m_grid[y][x] = OBSTRUCTED_POINT;
					obstructedPoints++;
				}
				else
					m_grid[y][x] = 0f;

		// System.out.println(obstructedPoints + " obstructed points");

		float totalPoints = (m_gridWidth * m_gridHeight) - obstructedPoints;
		float initialProbability = 1f / totalPoints;

		// initialise probs

		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getYSize(); y++)
				if (!isObstructed(x, y))
					m_grid[y][x] = initialProbability;
	}

	/**
	 * Constructs a new pose distribution from an existing one. The constructed distribution reuses the GridMap from the input distribution and has the same obstructed points.
	 *
	 * @param _that
	 */
	public GridPositionDistribution(GridPositionDistribution _that) {
		m_gridMap = _that.m_gridMap;
		m_gridWidth = m_gridMap.getXSize();
		m_gridHeight = m_gridMap.getYSize();

		m_grid = new Float[m_gridHeight][m_gridWidth];

		// flag obstructed points for future reference
		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getYSize(); y++)
				m_grid[y][x] = _that.m_grid[y][x];

	}

	/**
	 * Returns true if the given point is obstructed by an obstacle.
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	public boolean isObstructed(int _x, int _y) {
		return m_grid[_y][_x] == OBSTRUCTED_POINT;
	}

	/**
	 * Returns true if the input point is within the grid map and unobstructed.
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	public boolean isValidGridPosition(int _x, int _y) {
		return m_gridMap.isValidGridPosition(_x, _y) && !isObstructed(_x, _y);
	}

	/**
	 * Set the probability of the robot being at this grid point. This method only stores the given value and does not do anything with it or other values.
	 *
	 * @param _x
	 * @param _y
	 * @param _p
	 */
	public void setProbability(int _x, int _y, float _p) {
		assert m_gridMap.isValidGridPosition(_x, _y);
		assert !isObstructed(_x, _y);
		m_grid[_y][_x] = _p;
	}

	/**
	 * Get the probability of the robot being at the given grid point.
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	public float getProbability(int _x, int _y) {
		assert m_gridMap.isValidGridPosition(_x, _y);
		float p;
		if (isObstructed(_x, _y))
			p = 0;
		else
			p = m_grid[_y][_x];
		return p;
	}

	public IGridMap getGridMap() {
		return m_gridMap;
	}

	public int getGridHeight() {
		return m_gridHeight;
	}

	public int getGridWidth() {
		return m_gridWidth;
	}

	/**
	 * Normalise distribution so that it sums to 1.
	 */
	public void normalise() {
		float total = sumProbabilities();

		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getYSize(); y++)
				if (!isObstructed(x, y))
					m_grid[y][x] = m_grid[y][x] / total;

	}

	/**
	 * Get the sum of all probabilities in the grid
	 *
	 * @return
	 */
	public float sumProbabilities() {
		float total = 0;
		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getYSize(); y++)
				if (!isObstructed(x, y))
					total += m_grid[y][x];
		return total;
	}
}
