package rp.robotics.mapping;

import lejos.geom.Point;

/***
 * In interface to describe a map created from a regular grid of 2D positions, some of which can be obstructed. In the methods below then x,y positions refer to the position on the grid of the grid map. These have a minimum value of 0,0 and max of getXSize() - 1 and getYSize() - 1. The
 * implementation of this class must correctly map these positions to the positions on some underlying continuous map (i.e. an RPLineMap).
 *
 * @author Nick Hawes
 */
public interface IGridMap {

	/**
	 * Get the number of grid points in the x direction.
	 *
	 * @return
	 */
	public int getXSize();

	/**
	 * Get the number of grid points in the y direction.
	 *
	 * @return
	 */
	public int getYSize();

	/**
	 * Determines if the given x,y position is within the bounds of the grid map.
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	public boolean isValidGridPosition(int _x, int _y);

	/**
	 * Tests whether the given x,y position is obstructed by an obstacle in the map.
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	public boolean isObstructed(int _x, int _y);

	/**
	 * Convert the given x,y position to a 2D point on the underlying continuous map. This is n
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	public Point getCoordinatesOfGridPosition(int _x, int _y);

	/**
	 * Determines if there is an unobstructed route on the map between the two grid positions.
	 *
	 * @param _x1
	 * @param _y1
	 * @param _x2
	 * @param _y2
	 * @return
	 */
	public boolean isValidTransition(int _x1, int _y1, int _x2, int _y2);

	/**
	 * Reports the range to the nearest obstacle (i.e. wall). This is the equivalent of LineMap.range() but for a grid map.
	 *
	 * @param _x
	 * @param _y
	 * @param _heading The orientation of the robot in degrees.
	 * @return
	 */
	public float rangeToObstacleFromGridPosition(int _x, int _y, float _heading);

}
